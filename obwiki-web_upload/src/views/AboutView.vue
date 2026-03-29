
<template>
  <div class="about">
    <div class="content-container">
      <div class="decoration-top">INFORMATION</div>
      
      <h1 class="main-text">
        海洋生物知识库旨在建立一个集中式平台，为用户提供方便浏览、搜索和获取各类海洋生物知识的
        服务。通过该平台，用户可以随时随地获取感兴趣的海洋生物内容，并且允许用户创建、编辑和组织内容，使得大量海洋生物知识可以被集中记录、共享和更新。
      </h1>

      <div class="decoration-bottom"></div>
    </div>
  </div>
</template>

<style scoped>

.about {
  min-height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ffffff;
  padding: 40px 20px;
}

.content-container {
  max-width: 800px;
  position: relative;
  padding: 40px;
}

.decoration-top {
  font-family: 'Helvetica', sans-serif;
  font-size: 12px;
  letter-spacing: 4px;
  color: #e0e0e0;
  margin-bottom: 20px;
  font-weight: bold;
}

/* 核心内容排版 */
.main-text {
  margin: 0;
  font-weight: 400;
  line-height: 2; 
  color: #333333; 
  font-size: 1.25rem;
  text-align: justify;   
  word-break: break-all;
  letter-spacing: 1px;   
}


</style>