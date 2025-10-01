import axios from "axios";


const instance = axios.create({
    baseURL: "http://localhost:8080/api/game/nextMove",
})


interface Response {
    result: string,
    color: string,
    x: string,
    y: string
}


export const usersAPI = {
    async nextMove(size: number, data: string, nextPlayerColor: string): Promise<Response> {
        const response = await instance.post('', { size, data, nextPlayerColor });
        return response.data;
    }
}