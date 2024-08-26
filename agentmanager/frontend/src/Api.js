import axios from 'axios'

const SERVER_URL = 'http://localhost:8080/api/v1/';

const instance = axios.create({
    baseURL: SERVER_URL,
    timeout: 1000
});

export default {
    // (C)reate
    // createNew: (text, completed) => instance.post('agents', {title: text, completed: completed}),
    // (R)ead
    getAll: () => instance.get('agents', {
        transformResponse: [function (data) {
            return data? JSON.parse(response.data) : data;
        }]
    }),
    // (U)pdate
    // updateForId: (id, text, completed) => instance.put('agents/'+id, {title: text, completed: completed}),
    // (D)elete
    // removeForId: (id) => instance.delete('agents/'+id)
}