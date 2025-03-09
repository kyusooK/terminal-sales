<template>
    <v-card outlined @click="openDialog">
        <v-list dense v-if="value && value.length > 0">
            <v-list-item v-for="(spec, index) in referenceValues" :key="index">
                <v-list-item-content>
                    <v-list-item-title>Spec {{ spec._links.self.href.split('/').pop() }}</v-list-item-title>
                </v-list-item-content>
                <v-list-item-action v-if="editMode">
                    <v-btn icon @click="removeSpec(index)">
                        <v-icon>mdi-delete</v-icon>
                    </v-btn>
                </v-list-item-action>
            </v-list-item>
        </v-list>

        <v-card-actions v-if="editMode">
            <v-btn color="primary" text @click="openDialog">
                스펙 추가
            </v-btn>
        </v-card-actions>

        <v-dialog v-model="pickerDialog" width="500">
            <v-card>
                <v-card-title>Spec</v-card-title>
                <v-card-text>
                    <SpecPicker v-model="tempValue" @selected="pick"/>  <!-- value 대신 tempValue 사용 -->
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary" text @click="pickerDialog = false">Close</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-card>
</template>

<script>

    const axios = require('axios').default;
    import SpecPicker from './listers/SpecPicker.vue'; 

    export default {
        name: 'SpecId',
        components:{
            SpecPicker
        },
        props: {
            value: [Object, String, Number, Boolean, Array],
            editMode: Boolean,
            isNew: Boolean,
            offline: Boolean,
            inList: Boolean,
            label: String,
        },
        data: () => ({
            tempValue: null,
            referenceValues: [],
            pickerDialog: false,
        }),
        async created() {
            if (!this.value) {
                this.$emit('input', []);
            }
            await this.updateReferenceValues();
        },
        watch: {
            value(val) {
                this.$emit('input', val);
            },
            newValue(val) {
                this.$emit('input', val);
            },
        },

        methods: {
            edit() {
                this.editMode = true;
            },
            async add() {
                this.editMode = false;
                this.$emit('input', this.value);

                if(this.isNew){
                    this.$emit('add', this.value);
                } else {
                    this.$emit('edit', this.value);
                }
            },
            openDialog() {
                this.pickerDialog = true;
            },
            async pick(val) {
                if (!Array.isArray(this.value)) {
                    this.value = [];
                }
                
                // 객체 형태로 변경
                const specIdObj = { id: val.id };
                
                if (!this.value.some(item => item.id === val.id)) {
                    this.value = [...this.value, specIdObj];  // 객체를 배열에 추가
                    await this.updateReferenceValues();
                    this.$emit('input', this.value);
                    this.$emit('change', this.value);
                }
                this.pickerDialog = false;
            },
            
            async updateReferenceValues() {
                if (this.value && this.value.length > 0) {
                    this.referenceValues = await Promise.all(
                        this.value.map(async (specId) => {
                            const response = await axios.get(axios.fixUrl('/specs/' + specId.id));  // .id 추가
                            return response.data;
                        })
                    );
                }
            },
            removeSpec(index) {
                this.value.splice(index, 1);
                this.referenceValues.splice(index, 1);
                this.$emit('input', this.value);
                this.$emit('change', this.value);
            },
            async remove(){
                this.editMode = false;
                this.isDeleted = true;

                this.$emit('input', this.value);
                this.$emit('delete', this.value);
            },
            change(){
                this.$emit('change', this.value);
            },
        }
    }
</script>

