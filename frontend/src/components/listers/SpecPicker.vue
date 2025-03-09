<template>
    <div>
        <v-list two-line v-if="list.length > 0">
            <v-list-item-group 
                    v-model="selected" 
                    color="primary"
                    @change="select"
            >
                <v-list-item v-for="(item, idx) in list" :key="idx">
                    <template v-slot:default="{ active }">
                        <v-list-item-content>
                            <v-list-item-title>
                                Spec #{{item._links.self.href.split('/').pop()}}
                            </v-list-item-title>
                            <v-list-item-title>
                            </v-list-item-title>
                            <v-list-item-subtitle>
                                PhoneName :  {{item.phoneName }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                PhoneColor :  {{item.phoneColor }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                PhoneType :  {{item.phoneType }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                Image :  {{item.image }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                DetailSpec :  {{item.detailSpec }}
                            </v-list-item-subtitle>
                        </v-list-item-content>

                        <v-list-item-action>
                            <v-checkbox :input-value="active" color="primary-darker-1"></v-checkbox>
                        </v-list-item-action>
                    </template>
                </v-list-item>
            </v-list-item-group>
        </v-list>
    </div>
</template>


<script>
    const axios = require('axios').default;

    export default {
        name: 'SpecSpecPicker',
        props: {
            value: [String, Object, Array, Number, Boolean],
        },
        data: () => ({
            list: [],
            selected: null,
        }),
        // async created() {
        //     var me = this;
        //     var temp = await axios.get(axios.fixUrl('/specs'))
        //     if(temp.data) {
        //         me.list = temp.data._embedded.specs;
        //     }

        //     if(me.value && typeof me.value == "object" && Object.values(me.value)[0]) {
        //         var id = Object.values(me.value)[0];
        //         var tmpValue = await axios.get(axios.fixUrl('/specs/' + id))
        //         if(tmpValue.data) {
        //             var val = tmpValue.data
        //             me.list.forEach(function(item, idx) {
        //                 if(item.name == val.name) {
        //                     me.selected = idx
        //                 }
        //             })
        //         }
        //     }
        // },
        async created() {
            try {
                console.log("Fetching specs...");  // 디버깅용
                const response = await axios.get(axios.fixUrl('/specs'));
                console.log("Response:", response.data);  // 디버깅용
                
                if(response.data && response.data._embedded && response.data._embedded.specs) {
                    this.list = response.data._embedded.specs;
                } else {
                    console.log("No specs data found");  // 디버깅용
                    this.list = [];
                }
            } catch(e) {
                console.error("Error fetching specs:", e);
                this.list = [];
            }
        },
        // methods: {
        //     select(val) {
        //         var obj = {}
        //         if(val != undefined) {
        //             var arr = this.list[val]._links.self.href.split('/');
        //             obj['id'] = arr[4]; 
        //             this.$emit('selected', obj);
        //         }
        //     },
        // },
        methods: {
            select(val) {
                if(val !== undefined) {
                    const selectedSpec = this.list[val];
                    const specId = selectedSpec._links.self.href.split('/').pop();
                    this.$emit('selected', { id: specId });
                }
            },
        },
    };
</script>

