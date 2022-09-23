<script setup lang="ts">
import axios from"axios"
import {ref, defineProps, onMounted} from"vue"
import { useRouter } from "vue-router";

const props = defineProps({
postId:{
    type: [Number,String],
    require: true,
}
});
const post = ref({
id: 0,
title:"",
content: "",
});
onMounted(() =>{
axios.get(`/posts/${props.postId}`).then((response)=>{
console.log(response)
    post.value =  response.data;
});
});

const router =  useRouter()

const moveToEdit = () => {

    router.push({name: "edit", params: {postId: props.postId}});
}

</script>

<template>
 <el-row>
            <el-col>
    <h2 class="title">{{post.title}}</h2>
    <div class="sub">
             <div class="sub d-flex">
                <div class="category">개발</div>
                <div class="regDate">2022-09-25</div>
              </div>
    </div>
                 </el-col>
 </el-row>
    <el-row>
        <el-col>
            <div class= 'content'> {{post.content}} </div>
        </el-col>
     </el-row>
    <el-row>
        <el-col>
            <div class='d-flex justify-content-end'>
                <el-button type='warning' @click='moveToEdit()'>수정</el-button>
            </div>
        </el-col>
    </el-row>

</template>

<style scoped lang='scss'>
.title{
    font-size: 1.6rem;
    font-weight: 600;
    color: #383838;
}
.content{
    font-size: 0.95rem;
     margin-top: 8px;
      color: #7e7e7e;
      white-space: break-spaces;
      line-height: 4.5;
}

  .sub {
    margin-top: 4px;
    font-size: 0.8rem;
  }
  .regDate{
      margin-left: 10px;
      color: #6b6b6b;
    }

</style>