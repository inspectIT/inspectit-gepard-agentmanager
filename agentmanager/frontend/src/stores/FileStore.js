import { defineStore } from 'pinia';

export const useFileStore = defineStore('file', {
    state: () => ({
        fileContent: '',
    }),
    actions: {
        setFileContent(content) {
            this.fileContent = content;
        },
        getFileContent(){
            return this.fileContent
        }
    },
});