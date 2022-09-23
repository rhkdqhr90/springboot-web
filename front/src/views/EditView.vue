<script setup lang="ts">
import axios from"axios"
import {ref, defineProps, onMounted} from"vue"
import { useRouter } from "vue-router"

const router = useRouter()


const props = defineProps({

postId:{
    type: [Number,String],
    require: true,
},
});
const post = ref({
id: 0,
title:"",
content: "",
});

axios.get(`/posts/${props.postId}`).then((response)=>{
console.log(response)
    post.value =  response.data;
});


const edit = () =>{
    console.log(props.postId);
    axios.patch(`/posts/${props.postId}`,post.value).then(()=>{
        router.replace({name: "home"})
    });
}
</script>

<template>
<div>
<el-input v-model="post.title" placeholder="제목을 입력해 주시요"/>
</div>

<div class="mt-2">
<el-input v-model="post.content" type="textarea" :rows="15"/>
</div>
<el-button type="warning" @click="edit()">수정완료</el-button>
</template>

<style>

</style>