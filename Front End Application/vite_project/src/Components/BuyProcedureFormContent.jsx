import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';
import { Fragment } from 'react';
import Typography from '@mui/material/Typography';
import { useEffect, useState } from 'react';
import { getAvailableCars, updateStatus } from '../api/availableCars';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import {
    useNavigate,
} from "react-router-dom";
import { options } from '../common/utils';
import PendingIcon from '@mui/icons-material/Pending';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ThumbDownOffAltIcon from '@mui/icons-material/ThumbDownOffAlt';
import { parseJwt } from '../common/utils';

 const extractVehiclesDataFromResponse = (data) => {
    const vehicles = [];

    data.forms.forEach((element) =>
        vehicles.push(element.vehicle_ID)
    )

    return vehicles;
}
const statusIcons = () => ({
    "New": <PendingIcon />,
    "Completed": <CheckCircleIcon />,
    "Rejected": <ThumbDownOffAltIcon />

})

const pickformIDFromSelectedValue = (options, value) => {
    console.log('options', options);
    let formID;

    options.forEach(option => {
        if (option.vehicle_ID === value) {
            formID = option.form_ID;
        }
    })

    return formID;
}

const BuyProcedureContent = () => {
    const [currentTab, setCurrentTab] = useState(0);

    const [history, setHistory] = useState(null);

    const [selectedVehicle, setSelectedVehicle] = useState('');
    const [helperText, setHelperText] = useState('');

    const [ownerId, setOwnerId] = useState(null);
    const navigate = useNavigate();

    console.log('owner id ', selectedVehicle, ownerId);

    const [vehicles, setVehicles] = useState(null); // Sample list of vehicles

    const handleVehicleChange = (event) => {
        setSelectedVehicle(event.target.value);
    };

    const handleSubmit = async (status) => {
        try {

            let jwt = sessionStorage.getItem("jwt");

            const jwtData = parseJwt(jwt);
            console.log(jwtData);
            console.log(jwtData.email);
            
            const userName = jwtData.email;

            const body = {
                userID: userName,
                formID: pickformIDFromSelectedValue(history.forms,selectedVehicle),
                status: status,
            }

            const response = await updateStatus("/updateStatus",body);

            
            const params = {
                userId: userName,
                status: 'New'
            }
            
            const data = await getAvailableCars("/retrieve",params);

            setHistory(data);

            setVehicles(extractVehiclesDataFromResponse(data));

            setHelperText(response.data.message);

            console.log('response', response);
        }
        catch {
            setHelperText('error');
            console.log('ok')
        }


    }
    const handleTabChange = (event, newValue) => {
        setCurrentTab(newValue);
    };

    useEffect(() => {
        const getData = async () => {
            try {
                // Call the function to fetch available cars data

                let jwt = sessionStorage.getItem("jwt");

                const jwtData = parseJwt(jwt);
                console.log(jwtData);
                console.log(jwtData.email);
                
                const userName = jwtData.email;

                const params = {
                    userId: userName,
                    status: 'New'
                }

                const data = await getAvailableCars("/retrieve",params);//getVehicles

                setHistory(data);

                setVehicles(extractVehiclesDataFromResponse(data));

                console.log("Available cars:", data);
            } catch (error) {
                // Handle errors if any
                console.error("Error fetching available cars:", error);
            }
        };

        getData();
    }, []);
    return (
        <Fragment>
            <Grid item xs={12} >
                <Typography
                    component="h1"
                    variant="h6"
                    color="inherit"
                    noWrap
                    sx={{ textAlign: "center" }}
                >
                    Ολοκλήρωση Μεταβίβασης
                </Typography>
            </Grid>
            <Grid item xs={12} >
                <Tabs
                    value={currentTab}
                    onChange={handleTabChange}
                >
                    <Tab label="Available Cars for Transfer" sx={{ textAlign: "center" }} />
                </Tabs>
            </Grid>
            <Grid container item xs={12} spacing={2} >
                {/* Content for each tab goes here */}
                {currentTab === 0 && (
                    <>
                        <Grid item xs={5} mt={3}>
                            <FormControl fullWidth>
                                <InputLabel id="demo-simple-select-label">Choose the car you would like to transfer</InputLabel>
                                <Select
                                    value={selectedVehicle}
                                    onChange={handleVehicleChange}
                                    labelId="demo-simple-select-label"
                                    id="demo-simple-select"
                                    label={'Choose the car you would like to transfer'}
                                    fullWidth
                                >
                                    {vehicles && vehicles.map((vehicle, index) => (
                                        <MenuItem key={index} value={vehicle}>
                                            {vehicle}
                                        </MenuItem>
                                    ))}
                                </Select>
                                <Grid container spacing={2} sx={{ mt: 3 }}>
                                    <Grid item xs={6}>
                                        <Button variant="contained" fullWidth
                                            onClick={() => handleSubmit('Processed')}
                                            sx={{ textTransform: 'none ' }}
                                        >
                                            Ολοκλήρωση Μεταφοράς
                                        </Button>
                                    </Grid>
                                    <Grid item xs={6}>
                                        <Button variant="contained" fullWidth
                                            onClick={() => handleSubmit('Aborted')}
                                            sx={{ backgroundColor: 'red', textTransform : 'none ',color: 'white' }}
                                        >
                                            Ακύρωση Μεταφοράς
                                        </Button>
                                    </Grid>
                                </Grid>
                            </FormControl>

                        </Grid>
                        {/* <Grid item md={3} xs={8} >
                            <Button variant="contained" fullWidth
                                onClick={() => navigate('/sellCarForm', {
                                    state: { car: selectedVehicle }
                                })}
                            >
                            Επιλογή
                            </Button>
                        </Grid> */}
                    </>
                )}
                {/*{currentTab === 1 && (*/}
                {/*        history ? */}
                {/*            <TableContainer component={Paper}>*/}
                {/*                <Table>*/}
                {/*                    <TableHead>*/}
                {/*                        <TableRow>*/}
                {/*                            <TableCell>Date</TableCell>*/}
                {/*                            <TableCell>Buyer ID</TableCell>*/}
                {/*                            <TableCell>Owner ID</TableCell>*/}
                {/*                            <TableCell>Vehicle ID</TableCell>*/}
                {/*                            <TableCell>Form ID</TableCell>*/}
                {/*                            <TableCell>Status</TableCell>*/}
                {/*                        </TableRow>*/}
                {/*                    </TableHead>*/}
                {/*                    <TableBody>*/}
                {/*                        {history.forms.map((form, index) => (*/}
                {/*                            <TableRow key={index}>*/}
                {/*                                <TableCell>{(new Date(form.date)).toLocaleString('el-GR', options)}</TableCell>*/}
                {/*                                <TableCell>{form.buyer_ID}</TableCell>*/}
                {/*                                <TableCell>{form.owner_ID}</TableCell>*/}
                {/*                                <TableCell>{form.vehicle_ID}</TableCell>*/}
                {/*                                <TableCell>{form.form_ID}</TableCell>*/}
                {/*                                <TableCell> {statusIcons()[form.status]}</TableCell>*/}
                {/*                            </TableRow>*/}
                {/*                        ))}*/}
                {/*                    </TableBody>*/}
                {/*                </Table>*/}
                {/*            </TableContainer>*/}
                {/*            : null*/}
                {/*)}*/}
            </Grid>
        </Fragment>
    )
}

export default BuyProcedureContent;