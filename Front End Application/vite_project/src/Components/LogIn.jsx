import { useState } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import DirectionsCarIcon from '@mui/icons-material/DirectionsCar';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { loginUser } from '../api/login';
import { useNavigate } from "react-router-dom";

const defaultTheme = createTheme();

const LogIn = () => {

    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(false);

    const navigate = useNavigate();
    const endpoint = '/userAuth/login'
        
    const handleSubmit = async () => {
        try {
            const body = {
                userName: userName,
                password: password,
                clientId: "app-log-in",
                clientSecret: "**********"
            }
            const response = await loginUser(endpoint, body);

            sessionStorage.setItem("jwt", response.accessToken);

            sessionStorage.setItem("user", userName);

            navigate('/main');
        }
        catch {
            setError(true);
            console.log('unable to log in ');
        }
    }
 
    return (
        <ThemeProvider theme={defaultTheme}>

         <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }} >

                <CssBaseline />
                <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                        <DirectionsCarIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5">
                           Είσοδος
                    </Typography>
                <Box noValidate sx={{ mt: 1 }}
                >
                        <TextField
                            value={userName}
                            onChange={(event) => {
                                setUserName(event.target.value);
                                setError(false);
                            }}
                            margin="normal"
                            autoComplete='off'
                            required
                            fullWidth
                            label="Username"
                            name="Username"
                        />
                        <TextField
                            value={password}
                            onChange={(event) => { 
                                setError(false);
                                setPassword(event.target.value);
                            }}                       
                            margin="normal"
                            required
                            fullWidth
                            error={error}
                            autoComplete="new-password"

                            name="password"
                            label="Password"
                            type="password"
                            helperText={error && 'Παρακαλώ βάλτε σωστό κωδικό'  }
                        />
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            onClick={handleSubmit} 
                            sx={{ mt: 3, mb: 2 }}
                        >
                            Sign In
                        </Button>
                    </Box>
                </Box>
            </Container>
        </ThemeProvider>
    );
}

export default LogIn;