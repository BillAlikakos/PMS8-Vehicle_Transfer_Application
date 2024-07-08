import instance from "./apiInit";

export const getAvailableCars = async (endpoint,params) => {
    try {
        const response = await instance.get(endpoint, {
            params: params 
        });

        console.log('response is:', response);
        const { data } = response;

        return data;
    } catch (error) {
        console.error("Error fetching available cars:", error);

        throw error;
    }
}

export const postNewCar = async (endpoint, body) => {
    try {
        console.log(endpoint, body);
        const response = await instance.post(endpoint, body);

        return response;
    } catch (error) {
        console.error("Error submitting the form :", error);

        throw error;
    }
}

export const updateStatus = async (endpoint, body) => {
    try {
        const response = await instance.put(endpoint,body);

        const { data } = response;

        return data;
    } catch (error) {
        console.error("Error fetching available cars:", error);

        throw error;
    }
}