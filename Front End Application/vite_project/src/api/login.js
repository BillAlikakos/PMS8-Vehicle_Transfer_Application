import axios from 'axios';

const instance = axios.create({
    baseURL: '',
    timeout: 5000,
});

// Add a request interceptor
instance.interceptors.request.use(
    (config) => {
        return config;
    },
    (error) => {
        // Handle request error
        return Promise.reject(error);
    }
);


export const loginUser = async (endpoint, body) => {
    try {
        const response = await instance.post(endpoint, body);

        const { data } = response;

        return data;
    } catch (error) {
        console.error("Login Failed:", error);

        throw error;
    }

}