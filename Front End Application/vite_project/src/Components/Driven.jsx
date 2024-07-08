import React from 'react';
import { Tabs, Tab, AppBar, Toolbar, Typography, Container, Box } from '@mui/material';
import { DriveEtaOutlined, AccountBalanceWalletOutlined, DirectionsCarOutlined } from '@mui/icons-material';

function Dashboard() {
    const [value, setValue] = React.useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        Driven
                    </Typography>
                    <Typography variant="body2" color="inherit">
                        © {new Date().getFullYear()} Company Name. All rights reserved.
                    </Typography>
                </Toolbar>
            </AppBar>
            <Container>
                <Box sx={{ my: 2 }}>
                    <Tabs value={value} onChange={handleChange} centered>
                        <Tab label="Παρακολούθηση εξόδων" icon={<AccountBalanceWalletOutlined />} />
                        <Tab label="Παρακολούθηση συντήρησης οχημάτων" icon={<DirectionsCarOutlined />} />
                        <Tab label="Παρακολούθηση οδηγικής συμπεριφοράς" icon={<DriveEtaOutlined />} />
                    </Tabs>
                    <TabPanel value={value} index={0}>
                        <Typography variant="h5" gutterBottom>
                            Παρακολούθηση εξόδων
                        </Typography>
                        <Typography variant="body1">
                            Εδώ μπορείτε να δείτε τις τρέχουσες και προηγούμενες δαπάνες της εταιρείας σας, όπως τις καταχωρήσεις σε λογαριασμούς, μισθούς, και άλλα έξοδα.
                        </Typography>
                    </TabPanel>
                    <TabPanel value={value} index={1}>
                        <Typography variant="h5" gutterBottom>
                            Παρακολούθηση συντήρησης οχημάτων
                        </Typography>
                        <Typography variant="body1">
                            Σε αυτήν την καρτέλα μπορείτε να βλέπετε την κατάσταση συντήρησης των οχημάτων της εταιρείας, περιλαμβανομένων των ρυθμίσεων, των ελέγχων και των επιδιορθώσεων.
                        </Typography>
                    </TabPanel>
                    <TabPanel value={value} index={2}>
                        <Typography variant="h5" gutterBottom>
                            Παρακολούθηση οδηγικής συμπεριφοράς
                        </Typography>
                        <Typography variant="body1">
                            Εδώ μπορείτε να παρακολουθείτε τη συμπεριφορά οδήγησης των οδηγών της εταιρείας, όπως ο χρόνος οδήγησης, οι συχνότητες παύσης, και άλλες μετρικές ασφαλείας.
                        </Typography>
                    </TabPanel>
                </Box>
            </Container>
        </Box>
    );
}

function TabPanel(props) {
    const { children, value, index, ...other } = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`scrollable-auto-tabpanel-${index}`}
            aria-labelledby={`scrollable-auto-tab-${index}`}
            {...other}
        >
            {value === index && (
                <Box sx={{ p: 3 }}>
                    {children}
                </Box>
            )}
        </div>
    );
}

export default Dashboard;
