import { useNavigate } from "react-router-dom";
import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';
import { Fragment } from 'react';
import Typography from '@mui/material/Typography';

const SellProcedureContent = () => {

    const navigate = useNavigate();

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
                    Διάλεξε το επομένο βήμα
                </Typography>
            </Grid>
            <Grid item xs={12} sx={{ mt: 30, display: 'flex', flexDirection: 'row', }}>
                <Button
                    variant="contained"
                    color="primary"
                    sx={{
                        mx: 2,
                        height: '60px', // Adjusts the height of the button
                    }}
                    fullWidth
                    style={{ textTransform: 'none' }}
                    onClick={() => navigate('/availableCars')}

                >
                    Διαθέσιμα Αυτοκίνητα προς Πώληση
                </Button>
                <Button
                    variant="contained"
                    color="secondary"
                    sx={{
                        mx: 2,
                        height: '60px', // Adjusts the height of the button
                    }}
                    fullWidth
                    style={{ textTransform: 'none' }}
                    onClick={() => navigate('/sellerTransferCar')}

                >
                    Εκκίνηση Διαδικασίας Πώλησης
                </Button>
            </Grid>
        </Fragment>
    )
}

export default SellProcedureContent