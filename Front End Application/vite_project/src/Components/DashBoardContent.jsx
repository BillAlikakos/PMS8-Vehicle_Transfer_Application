import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';
import { Fragment } from 'react';
import Typography from '@mui/material/Typography';
import { useNavigate } from "react-router-dom";


const DashBoardContent = () => {
    const navigate = useNavigate()
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
                Διάλεξε την υπηρεσία σου
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
                    onClick={() => navigate('/sell-procedure') }

            >
                Διαδικασία Πώλησης
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
                onClick={() => navigate('/buy-procedure')}
            >
                Διαδικασία Αγοράς
            </Button>
            </Grid>
        </Fragment>
    )
}

export default DashBoardContent