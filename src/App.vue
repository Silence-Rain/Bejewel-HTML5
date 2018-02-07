<template>
  <div id="app">
    <div class="left">
      <Scoreboard/>
      <CtrlPanel/>
    </div>
    <div class="right">
      <MainScene/>
    </div>
  </div>
</template>

<script>
import MainScene from './components/MainScene'
import Scoreboard from './components/Scoreboard'
import CtrlPanel from './components/CtrlPanel'

export default {
  name: 'App',
  components: {
    MainScene,
    Scoreboard,
    CtrlPanel
  }
}
</script>

<style scoped>
#app {
  display: flex;
  flex-direction: row;
  font-family: "Microsoft Yahei UI";
}

.left {
  width: 300px;
  margin: 0 20px;
  display: flex;
  flex-direction: column;
}

.right {
  width: 700px;
  margin: 0 20px;
}
</style>
