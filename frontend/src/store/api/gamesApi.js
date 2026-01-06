import axios from "axios";
import {orderBy} from "lodash";

axios.defaults.withCredentials = true;

const gamesApi = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL
})

export const getGames = async ({signal}) => {
    const response = gamesApi.get("/game/all", {
        data: signal,
        transformResponse: (data) => {
            return  orderBy(JSON.parse(data), datum => new Date(datum.lastModifiedDate), "desc")
        }
    })
    return (await response).data
}

export const getSingleGame = async ({signal, id}) => {
    const response = gamesApi.get(`/game/${id}`, signal)
    return (await response).data
}

export const deleteSingleGame = async ({signal, id}) => {
    const response = gamesApi.delete(`/game/${id}`, {signal})
    return (await response).data
}


export const addGame = async (gameData) => {
    return await gamesApi.post("/game/new", gameData)
}

export const saveGame = async (gameData) => {
    return await gamesApi.put("/game/save", gameData)
}