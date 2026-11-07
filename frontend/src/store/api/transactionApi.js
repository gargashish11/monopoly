import axios from "axios";

const transactionApi = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL || "http://localhost:3999",
    withCredentials: true
})

export const addTransaction = async (transactionData) => {
    const response = await transactionApi.post("/transaction/add", transactionData)
    return response.data;
}