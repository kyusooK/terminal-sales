<template>

    <v-data-table
        :headers="headers"
        :items="getTerminal"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'GetTerminalView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            getTerminal : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/getTerminals'))

            temp.data._embedded.getTerminals.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.getTerminal = temp.data._embedded.getTerminals;
        },
        methods: {
        }
    }
</script>

