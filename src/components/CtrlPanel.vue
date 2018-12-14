<template>
	<div id="panel">
		<div class="hint" @click="hint">Hint</div>
		<div class="btn" @click="save">Save</div>
		<!-- <div class="btn" @click="setting">Settings</div> -->
		<div class="btn" @click="report" style="font-size:25px;">Report Bugs</div>
	</div>
</template>

<script>
	import axios from 'axios'

	export default {
		name: "CtrlPanel",

		props: {
			score: Number,
			mat: String
		},

		methods: {
			//给用户下一步的提示
			hint () {
				axios.post(this.baseUrl+"/hint", JSON.stringify({matrix: this.mat}))
					.then(response => {
						let position = response.data.result.res

						if (position == "error") {
							alert("Hint模块出错，请将bug反馈到daniel.s.mo503@gmail.com")
						} else if (position[0] == -1 && position[1] == -1) {
							alert("找不到可以交换的位置，游戏结束！")
						} else {
							alert(`提示位置：第${position[0]}行第${position[1]}列`)
							position[0] -= 1
							position[1] -= 1
							this.$parent.$emit("hint", JSON.stringify(position))
						}
					})
					.catch(response => alert("网络错误，请稍后再试"))
			},

			save () {
				axios.post(this.baseUrl+"/save", {score: this.score})
					.then(response => alert("保存成功"))
					.catch(response => alert("保存失败，请稍后再试"))
			},

			setting () {

			},

			//报告bug，向后端发送邮件请求
			report () {
				alert("请发送邮件至daniel.s.mo503@gmail.com")

				//TODO: 向后端发送邮件请求
			}
		}
	}
</script>

<style scoped>
#panel {
	height: 440px;
	display: flex;
	flex-direction: column;
	align-items: center;
}

#panel div {
	text-align: center;
	font-size: 30px;
}

.hint {
	width: 160px;
	height: 160px;
	margin: 20px 70px;
	border-radius: 80px;
	background: -webkit-gradient(linear, 0 0, right bottom, from(#1E90FF), to(#1E90FF), color-stop(0.5, #000080));
	line-height: 160px;
	color: #FFF;
}

.btn {
	width: 200px;
	height: 60px;
	margin: 10px 50px;
	border-radius: 30px;
	border: 1px solid #000;
	line-height: 60px;
}
</style>