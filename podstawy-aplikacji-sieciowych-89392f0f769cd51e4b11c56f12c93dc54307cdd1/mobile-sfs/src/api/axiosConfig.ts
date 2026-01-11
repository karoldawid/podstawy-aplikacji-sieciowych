import axios from 'axios';


const YOUR_IP_ADDRESS = '192.168.33.9';

const apiClient = axios.create({
        baseURL: `http://${YOUR_IP_ADDRESS}:8080/api/v1`,
    headers: {
        'Content-Type': 'application/json',
        },
});

export default apiClient;