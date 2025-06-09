<template>
    hello! this is the home page
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { useUserStore } from '../store/user';

const userStore = useUserStore()
const { token, username } = storeToRefs(userStore)
console.log(token)
console.log(username)
</script>