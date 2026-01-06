import axios from "axios";

const transactionApi = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL
})

export const addTransaction = async (transactionData) => {
    const response = await transactionApi.put("/transaction/add", transactionData)
    return (await response).data;
}