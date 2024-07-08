package com.bill.notificationService.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.bill.notificationService.dataTypes.FormData;
import com.bill.notificationService.dataTypes.NotificationRequest;
import com.bill.notificationService.util.Constants;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class NotificationService
{
    @Autowired
    private JavaMailSender mailSender;

    @Value("${sender-email}")
    private String sender;
    
    @Value("${BUYER_NOTIFICATION_URL}")
    private String redirectUrl;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    private Logger logger = LogManager.getLogger();
    
    @Async
    public void sendNotification(NotificationRequest request) throws MessagingException, IOException 
    {
    	String uuid = java.util.UUID.randomUUID().toString();
    	MimeMessage message = mailSender.createMimeMessage();
    	message.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML.toString());
        
        MimeMessageHelper msh = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        
        msh.setFrom(sender);
        msh.setTo(request.getRecipient());
        if(request.getIsOwner())
        {
        	msh.setSubject(Constants.OWNER_SUBJECT);
        	
        	String result = request.getIsApproved() ? Constants.FORM_STATUS_APPROVED : Constants.FORM_STATUS_REJECTED;
        	msh.setText("", MessageFormat.format(Constants.OWNER_FORM_TEMPLATE, request.getFormID(), result));
        }
        else if(request.getIsComplete())
        {	
        	msh.setSubject(Constants.COMPLETE_SUBJECT);
        	msh.setText("",Constants.BUYER_FORM_COMPLETE_TEMPLATE);
        	
        	logger.debug("Getting message template");
        	Resource vehicleRegistrationTemplate = resourceLoader.getResource("classpath:templates"+File.separator+"VehicleRegistration.html");
        	
        	logger.debug("Parsing HTML Document");
        	String content = "";
            try (Scanner scanner = new Scanner(vehicleRegistrationTemplate.getInputStream(), StandardCharsets.UTF_8.name())) {
                content = scanner.useDelimiter("\\A").next();
            }

            //Escape curly braces in order to format
            logger.debug("Escaping HTML Document css");
            Pattern pattern = Pattern.compile("(?s)<style.*?>(.*?)</style>");
            Matcher matcher = pattern.matcher(content);
            StringBuffer sb = new StringBuffer();
            
			while (matcher.find()) 
			{
                String cssContent = matcher.group(1);
                cssContent = cssContent.replace("{", "'{'").replace("}", "'}'");
                matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group().replace(matcher.group(1), cssContent)));
            }

            matcher.appendTail(sb);
            content = sb.toString();
            logger.info("HTML Document: "+content);
            
            FormData data = request.getData();
            logger.debug("Formatting document content");
            
            Date date = data.getDateOfInspection();
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);  
            String strDate = dateFormat.format(date);
            
        	content = MessageFormat.format(content, data.getVehicleID(), data.getFirstName(), data.getLastName(), data.getMake(), data.getModel(), String.valueOf(data.getYear()), data.getDisplacement()+"cc", data.getColor(), strDate);
        	logger.info("Formatted HTML Document: "+content);
        	
        	Document document = Jsoup.parse(content, StandardCharsets.UTF_8.name());
        	
        	logger.debug("Setting document settings");
        	document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        	
        	logger.debug("Converting document to pdf");
        	convertToPDF(document, uuid);
        	
        	File pdfFile = new File(uuid+Constants.PDF_EXTENSION);
        	byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath()); 
        	
        	logger.debug("Adding pdf attachment. File bytes size: "+pdfBytes.length+"\nPath: "+pdfFile.toPath());
        	msh.addAttachment(uuid, new ByteArrayDataSource(pdfBytes, MediaType.APPLICATION_PDF_VALUE));
        	
        	if(pdfFile.exists())
        	{
        		pdfFile.delete();
        	}
        }
        else
        {
        	msh.setSubject(Constants.BUYER_SUBJECT);
        	String redirect = MessageFormat.format(Constants.BUYER_REDIRECT,  this.redirectUrl);
        	
        	msh.setText("",MessageFormat.format(Constants.BUYER_FORM_TEMPLATE, request.getFormID(), redirect));
        }
        
        mailSender.send(message);
    }
    
    private void convertToPDF(Document inputHTML, String uuid) throws IOException 
    {
    	
    	File outputPdf = new File(uuid+Constants.PDF_EXTENSION);
    	
    	if(!outputPdf.exists())
    		outputPdf.createNewFile();
    	
    	try (OutputStream outputStream = new FileOutputStream(outputPdf)) 
    	{
    	    ITextRenderer renderer = new ITextRenderer();
    	    SharedContext sharedContext = renderer.getSharedContext();
    	    sharedContext.setPrint(true);
    	    sharedContext.setInteractive(false);
    	    renderer.setDocumentFromString(inputHTML.html());
    	    renderer.layout();
    	    renderer.createPDF(outputStream);
    	} catch (FileNotFoundException e)
		{
    		logger.error("Caught FileNotFoundException while converting HTML Document to pdf");
			e.printStackTrace();
		} catch (IOException e)
		{
			logger.error("Caught IOException while converting HTML Document to pdf");
			e.printStackTrace();
			
			if(outputPdf.exists())
			{
				outputPdf.delete();
			}
			
		}
    	
    }
    
}