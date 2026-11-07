import axios from "axios";
import {orderBy} from "lodash";

const gamesApi = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL || "http://localhost:3999",
    withCredentials: true
})

export const getGames = async ({signal} = {}) => {
    const response = await gamesApi.get("/game/all", {
        signal,
        transformResponse: [
            ...axios.defaults.transformResponse,
            (data) => {
                if (!Array.isArray(data)) return [];
                return orderBy(data, datum => datum.lastModifiedDate ? new Date(datum.lastModifiedDate) : new Date(0), "desc")
            }
        ]
    })
    return response.data
}

export const getSingleGame = async ({signal, id} = {}) => {
    const response = await gamesApi.get(`/game/${id}`, {signal})
    return response.data
}

export const deleteSingleGame = async ({signal, id} = {}) => {
    const response = await gamesApi.delete(`/game/${id}`, {signal})
    return response.data
}

export const addGame = async (gameData) => {
    return await gamesApi.post("/game/new", gameData)
}

export const saveGame = async (gameData) => {
    return await gamesApi.put("/game/save", gameData)
}