import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';
import { Fragment } from 'react';
import Typography from '@mui/material/Typography';
import { useEffect, useState } from 'react';
import { getAvailableCars } from '../api/availableCars';
import TableContainer from '@mui/material/TableContainer';
import Table from '@mui/material/Table';
import TableHead from '@mui/material/TableHead';
import TableCell from '@mui/material/TableCell';
import TableRow from '@mui/material/TableRow';
import TableBody from '@mui/material/TableBody';
import Paper from '@mui/material/Paper';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import {
    useNavigate,
} from "react-router-dom";
import { extractVehiclesDataFromResponse, options } from '../common/utils';
import PendingIcon from '@mui/icons-material/Pending';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ThumbDownOffAltIcon from '@mui/icons-material/ThumbDownOffAlt';
import { parseJwt } from '../common/utils';

const statusIcons = () => ({
    "New": <PendingIcon />,
    "Completed": <CheckCircleIcon />,
    "Rejected": <ThumbDownOffAltIcon />

})

const pickOwnerIdFromSelectedValue = (options, value) => {

    let ownerId;

    options.forEach(option => {
        if (option.vehicleId === value) {
            ownerId = option.ownerId;
        }
    })

    return ownerId;
}

const SellerTransferCarContent = () => {
    const [currentTab, setCurrentTab] = useState(0);

    const [history, setHistory] = useState(null);

    const [selectedVehicle, setSelectedVehicle] = useState('');

    const [ownerId, setOwnerId] = useState(null);
    const navigate = useNavigate();

    console.log('owner id ', selectedVehicle, ownerId);

    const [vehicles, setVehicles] = useState(null); // Sample list of vehicles

    const handleVehicleChange = (event) => {
        setSelectedVehicle(event.target.value);
        setOwnerId(pickOwnerIdFromSelectedValue(history.vehicles, event.target.value));
    };

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
                    userId: userName
                }
                
                const data = await getAvailableCars("/getVehicles",params);

                console.log('data', data);

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
                    Μεταβίβαση Οχήματος
                </Typography>
            </Grid>
            <Grid item xs={12} >
                <Tabs
                    value={currentTab}
                    onChange={handleTabChange}
                >
                    <Tab label="Available Cars for Transfer" sx={{ textAlign: "center" }}/>
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
                                <Button variant="contained" fullWidth
                                    onClick={() => navigate('/sellCarForm', {
                                        state: { car: selectedVehicle, ownerId: ownerId }
                                    })}
                                    sx={{ mt: 3 } }
                                >
                                    Επιλογή
                                </Button>
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

export default SellerTransferCarContent