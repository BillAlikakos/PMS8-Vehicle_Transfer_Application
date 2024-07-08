import { Fragment, useState } from "react"
import Typography from '@mui/material/Typography';
import Alert from '@mui/material/Alert';
import Grid from '@mui/material/Grid';
import dayjs, { Dayjs } from 'dayjs';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateField } from '@mui/x-date-pickers/DateField';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import {
    useNavigate, useLocation
} from "react-router-dom";
// import { randomString } from "../common/utils";
import { postNewCar } from "../api/availableCars";
import FormHelperText from '@mui/material/FormHelperText';



const SellCarFormContent = () => {
    let { state } = useLocation();

    console.log('state', state);

    const [dateValue, setDateValue] = useState(null)
    const [afm, setAfm] = useState(null);
    const navigate = useNavigate();

    const endpoint = '/submit';

    const [helperText, setHelperText] = useState('');

    console.log('helper text', helperText);
    const handleSubmit = async () => {
        try {
            const body = {
                date: dateValue, 
                ownerID: state.ownerId,
                vehicleID: state.car,
                taxID: afm,
                status: "New"
            }

            console.log('random string worked');

            const response = await postNewCar(endpoint, body);

            console.log('response ', response);

            setHelperText(response.data.message);

            console.log('response', response);
        }
        catch {
            setHelperText('error');
            console.log('ok')
        }

        
    }
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
                Φόρμα Καταχώρησης Αυτοκινήτου
            </Typography>
            </Grid>

            <Grid item xs={12}>

                <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DatePicker
                        label="Ημερομηνία Καταχώρισης"
                        value={dayjs(new Date())}
                        onChange={(newValue) => setDateValue(newValue)}

                    />
                </LocalizationProvider>
            </Grid>

            <Grid item xs={12}>
                <TextField
                    id="afm"
                    label="AFM"
                    fullWidth
                    margin="normal"
                    value={afm}
                    onChange={(e) => setAfm(e.target.value)}
                />
            </Grid>

            <Button variant="contained" fullWidth
                onClick={handleSubmit}
                sx={{ mt: 3 }}

            >
                Επιλογή
            </Button>
            {helperText ? helperText === 'error' ? <Alert severity="error">{helperText}</Alert> : <Alert severity="success">{helperText}</Alert> : null}
        </Fragment>
    )
}

export default SellCarFormContent