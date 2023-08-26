const { createApp } = Vue;

createApp({
    data() {
        return {
            clientInfo: {},
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData() {

            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                })
                .catch((error) => {
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function (event) {
                    axios.post('/api/logout')
                        .then(response => window.location.href = "/web/index.html")
                        .catch(() => {
                            this.errorMsg = "Sing Out failed, check the information"
                            this.errorToats.show();
                        })
        },
        create: function (event) {
                    axios.post('/api/clients/current/accounts')
                        .then(response => window.location.reload())
                        .catch((error) => {
                            this.errorMsg = error.response.data;
                            this.errorToats.show();
                        })
        }
    },
    mounted() {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app');