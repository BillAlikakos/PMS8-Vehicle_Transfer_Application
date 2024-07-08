import axios from 'axios';

const instance = axios.create({
    baseURL: '/',
    timeout: 3500,
});

// Add a request interceptor
instance.interceptors.request.use(
    (config) => {
        // Modify config or add headers before request is sent
        config.headers['Authorization'] = sessionStorage.getItem("jwt");
        return config;
    },
    (error) => {
        // Handle request error
        return Promise.reject(error);
    }
);


export default instance;