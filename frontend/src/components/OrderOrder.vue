<template>
    <v-card outlined>
        <template slot="progress">
            <v-progress-linear
                    color="primary-darker-1"
                    height="10"
                    indeterminate
            ></v-progress-linear>
        </template>

        <v-card-title v-if="value._links">
            주문 # {{decode(value._links.self.href.split("/")[value._links.self.href.split("/").length - 1])}}
        </v-card-title >
        <v-card-title v-else>
            주문
        </v-card-title >        

        <v-card-text style="background-color: white;">
            <String label="주소" v-model="value.address" :editMode="editMode" :inputUI="''"/>
            <Boolean label="파손보험" v-model="value.breakInsurance" :editMode="editMode" :inputUI="''"/>
            <Boolean label="분실보험" v-model="value.lostInsurance" :editMode="editMode" :inputUI="''"/>
            <SpecId offline label="specId" v-model="value.specId" :editMode="editMode" @change="change"/>
            <UserId offline label="userId" v-model="value.userId" :editMode="editMode" @change="change"/>
            <Number label="주문금액" v-model="value.price" :editMode="editMode" :inputUI="''"/>
            <Number label="결제정보" v-model="value.paymentId" :editMode="editMode" :inputUI="''"/>
            <String label="결제상태" v-model="value.paymentStatus" :editMode="editMode" :inputUI="''"/>
        </v-card-text>

        <v-card-actions style="background-color: white;">
            <v-spacer></v-spacer>
            <div v-if="!editMode">
                <v-row>
                    <payment-system-app>
                        <payment-system
                            service-type="pay"
                            :request-info="JSON.stringify(paymentData)" 
                            buyer-info-mode="true"
                        ></payment-system>
                    </payment-system-app>
                    <v-btn
                        color="primary"
                        text
                        @click="edit"
                    >
                        수정
                    </v-btn>
                    <v-btn
                        color="primary"
                        text
                        @click="remove"
                    >
                        삭제
                    </v-btn>
                    <payment-system-app v-if="value.paymentId">
                        <payment-system
                            service-type="receipt"
                            :request-info="JSON.stringify(paymentData)" 
                            buyer-info-mode="true"
                        ></payment-system>
                    </payment-system-app>
                </v-row>
            </div>
            <div v-else>
                <v-btn
                    color="primary"
                    text
                    @click="save"
                >
                    주문
                </v-btn>
                <v-btn
                    color="primary"
                    text
                    @click="editMode = false"
                    v-if="editMode && !isNew"
                >
                    취소
                </v-btn>
            </div>
        </v-card-actions>
        <v-card-actions>
            <v-spacer></v-spacer>
        </v-card-actions>

        <v-snackbar
            v-model="snackbar.status"
            :top="true"
            :timeout="snackbar.timeout"
            color="error"
        >
            {{ snackbar.text }}
            <v-btn dark text @click="snackbar.status = false">
                Close
            </v-btn>
        </v-snackbar>
    </v-card>

</template>

<script>
    const axios = require('axios').default;


    export default {
        name: 'OrderOrder',
        components:{
        },
        props: {
            value: [Object, String, Number, Boolean, Array],
            editMode: Boolean,
            isNew: Boolean,
            offline: Boolean,
        },
        data: () => ({
            snackbar: {
                status: false,
                timeout: 5000,
                text: '',
            },
            paymentData: null,
        }),
	async created() {
            if(!this.paymentData){
                this.paymentData = {
                    itemId : this.decode(this.value._links.self.href.split("/")[this.value._links.self.href.split("/").length - 1]),
                    price: this.value.fee,
                    name: "주문금액액",
                    buyerId: "user",
                    buyerEmail: "user@gmail.com",
                    buyerTel: "01012345678",
                    buyerName: "user"
                }
            }
        },
        methods: {
            decode(value) {
                return decodeURIComponent(value);
            },
            selectFile(){
                if(this.editMode == false) {
                    return false;
                }
                var me = this
                var input = document.createElement("input");
                input.type = "file";
                input.accept = "image/*";
                input.id = "uploadInput";
                
                input.click();
                input.onchange = function (event) {
                    var file = event.target.files[0]
                    var reader = new FileReader();

                    reader.onload = function () {
                        var result = reader.result;
                        me.imageUrl = result;
                        me.value.photo = result;
                        
                    };
                    reader.readAsDataURL( file );
                };
            },
            edit() {
                this.editMode = true;
            },
            async save(){
                try {
                    var temp = null;

                    if(!this.offline) {
                        if(this.isNew) {
                            temp = await axios.post(axios.fixUrl('/orders'), this.value)
                        } else {
                            temp = await axios.put(axios.fixUrl(this.value._links.self.href), this.value)
                        }
                    }

                    if(this.value!=null) {
                        for(var k in temp.data) this.value[k]=temp.data[k];
                    } else {
                        this.value = temp.data;
                    }

                    this.editMode = false;
                    this.$emit('input', this.value);

                    if (this.isNew) {
                        this.$emit('add', this.value);
                    } else {
                        this.$emit('edit', this.value);
                    }

                    location.reload()

                } catch(e) {
                    this.snackbar.status = true
                    if(e.response && e.response.data.message) {
                        this.snackbar.text = e.response.data.message
                    } else {
                        this.snackbar.text = e
                    }
                }
                
            },
            async remove(){
                try {
                    if (!this.offline) {
                        await axios.delete(axios.fixUrl(this.value._links.self.href))
                    }

                    this.editMode = false;
                    this.isDeleted = true;

                    this.$emit('input', this.value);
                    this.$emit('delete', this.value);

                } catch(e) {
                    this.snackbar.status = true
                    if(e.response && e.response.data.message) {
                        this.snackbar.text = e.response.data.message
                    } else {
                        this.snackbar.text = e
                    }
                }
            },
            change(){
                this.$emit('input', this.value);
            },
        },
    }
</script>

