package com.bill.userAuth.dataTypes;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public  class User
{
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified;
    private long createdTimestamp;
    private boolean enabled;
    private boolean totp;
    private Map<String, List<String>> attributes;
    private Access access;

    public String getId() 
    {
        return id;
    }

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getFirstName() 
    {
        return firstName;
    }

    public void setFirstName(String firstName) 
    {
        this.firstName = firstName;
    }

    public String getLastName() 
    {
        return lastName;
    }

    public void setLastName(String lastName) 
    {
        this.lastName = lastName;
    }

    public String getEmail()
{
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean isEmailVerified() 
    {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified)
    {
        this.emailVerified = emailVerified;
    }

    public long getCreatedTimestamp()
    {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) 
    {
        this.createdTimestamp = createdTimestamp;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isTotp() 
    {
        return totp;
    }

    public void setTotp(boolean totp)
    {
        this.totp = totp;
    }

    public Map<String, List<String>> getAttributes() 
    {
        return attributes;
    }

    public void setAttributes(Map<String, List<String>> attributes)
    {
        this.attributes = attributes;
    }

    public Access getAccess() 
    {
        return access;
    }

    public void setAccess(Access access)
    {
        this.access = access;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Access 
{
    private boolean manageGroupMembership;
    private boolean view;
    private boolean mapRoles;
    private boolean impersonate;
    private boolean manage;

    public boolean isManageGroupMembership() 
    {
        return manageGroupMembership;
    }

    public void setManageGroupMembership(boolean manageGroupMembership) 
    {
        this.manageGroupMembership = manageGroupMembership;
    }

    public boolean isView() 
    {
        return view;
    }

    public void setView(boolean view)
    {
        this.view = view;
    }

    public boolean isMapRoles() 
    {
        return mapRoles;
    }

    public void setMapRoles(boolean mapRoles)
    {
        this.mapRoles = mapRoles;
    }

    public boolean isImpersonate() 
    {
        return impersonate;
    }

    public void setImpersonate(boolean impersonate) 
    {
        this.impersonate = impersonate;
    }

    public boolean isManage() 
    {
        return manage;
    }

    public void setManage(boolean manage) 
    {
        this.manage = manage;
    }
}