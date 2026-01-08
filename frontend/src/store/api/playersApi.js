import axios from "axios";

const playersApi = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL
})

export const getPlayers = async ({signal}) => {
    const response = await playersApi.get("/player/all", signal)
    return response.data;
}

export const deletePlayer = async ({signal, id}) => {
    const response = await playersApi.delete(`/player/${id}`, {signal})
    return response.data
}

export const updatePlayer = async ({signal, id, player}) => {
    const response = await playersApi.patch(`/player/${id}`, player, {signal})
    return response.data
}
