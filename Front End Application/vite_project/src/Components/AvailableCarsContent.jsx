import Grid from '@mui/material/Grid';
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
import PendingIcon from '@mui/icons-material/Pending';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ThumbDownOffAltIcon from '@mui/icons-material/ThumbDownOffAlt';
import { options } from '../common/utils';
import { parseJwt } from '../common/utils';

const statusIcons = () => ({
    "INPROGRESS": <PendingIcon />,
    "AVAILABLE": <CheckCircleIcon />, 
})


const AvailableCarsContent = () => {
    const [cars, setCars] = useState(null)


    
    useEffect(() => {
        const getData = async () => {
            try {
                // Call the function to fetch available cars data
                // const userName = sessionStorage.getItem("user");
                let jwt = sessionStorage.getItem("jwt");

                const jwtData = parseJwt(jwt);
                console.log(jwtData);
                console.log(jwtData.email);
                
                const userName = jwtData.email;

                const params = {
                    userId: userName
                }

                const data = await getAvailableCars("/getVehicles",params);
                console.log("Available cars:", data);

                setCars(data.vehicles);
                // Handle the retrieved data
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
                    Τα διαθέσιμα αυτοκίνητα σου
                </Typography>
            </Grid>
            <Grid item xs={12}>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Brand</TableCell>
                                <TableCell>Model</TableCell>
                                <TableCell>Color</TableCell>
                                <TableCell>DateOfInspection</TableCell>
                                <TableCell>Displacement</TableCell>
                                <TableCell>Vehicle ID</TableCell>
                                <TableCell>Year</TableCell>
                                <TableCell>Status</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {cars && cars.map((form, index) => (
                                <TableRow key={index}>
                                    <TableCell>{form.make}</TableCell>
                                    <TableCell>{form.model}</TableCell>
                                    <TableCell>{form.color}</TableCell>
                                    <TableCell>{form.dateOfInspection}</TableCell>
                                    <TableCell>{form.displacement}</TableCell>
                                    <TableCell>{form.vehicleId}</TableCell>
                                    <TableCell>{form.year}</TableCell>
                                    <TableCell> {statusIcons()[form.status]}</TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
                
            </Grid>
        </Fragment>
    )
}

export default AvailableCarsContent