<template>
	<div id="scene">
		<!-- <transition-group name="slide-fade"> -->
		<div class="row" v-for="(row,index) in matrix" :key="index">
			<div class="gem" :x="item.x" :y="item.y" :style="{backgroundColor: item.color}" :class="{selected: item.isSelected}" @click="selectGem" v-for="item in row">{{item.val}}</div>
		</div>
		<!-- </transition-group> -->
	</div>
</template>

<script>
	export default {
		name: "MainScene",
		data () {
			return {
				matrix: [],																																					//宝石矩阵
				colors: ["#FE5C5C", "#52FD52", "#136BDE", "#D0D029", "#FEA33F", "#D41DD4", "#FFF"],	//宝石颜色枚举
				selected: false,																																		//当前是否有宝石被选中
				first: {},																																					//第一次选中的宝石坐标
				second: {}																																					//第二次选中的宝石坐标
			}
		},

		//初始化，随机填充宝石矩阵（不能出现可消除的情况）
		created () {
			for (var i = 0; i < 8; i++) {
				var temp = []
				for (var j = 0; j < 8; j++) {
					var obj = {}
					obj.val = Math.ceil(Math.random() * 7)		//宝石的值（即颜色）
					obj.color = this.colors[obj.val - 1]			//宝石颜色
					obj.isSelected = false										//宝石被选中的状态
					obj.x = i 																//当前宝石横坐标
					obj.y = j																	//当前宝石纵坐标

					temp.push(obj)
				}
				this.matrix.push(temp)
			}

			//TODO：排除可消除的情况
		},

		methods: {
			//选中宝石
			//若是第一次选中，记录选中坐标并改变其选中状态
			//若是第二次选中，先检查能否交换
			//		若能交换，检查能否消除
			//				若能消除则消除，不能消除返回未交换状态
			//		若不能交换，取消对第一次选中的记录，并将当前（第二次）选中的结果记为第一次
			selectGem (event) {
				if (!this.selected) {
					this.selected = true
					this.first.x = Number(event.target.attributes.x.value)
					this.first.y = Number(event.target.attributes.y.value)
					this.matrix[this.first.x][this.first.y].isSelected = true
				}
				else {
					this.second.x = Number(event.target.attributes.x.value)
					this.second.y = Number(event.target.attributes.y.value)

					if (this.first.x == this.second.x && this.first.y == this.second.y) {
						this.matrix[this.second.x][this.second.y].isSelected = false
						return
					}

					this.matrix[this.second.x][this.second.y].isSelected = true

					if (this.checkGemConnection(this.first, this.second)) {
						this.swap(this.first, this.second)

						let res1 = this.checkGemElimination(this.first)
						let res2 = this.checkGemElimination(this.second)

						if (res1.flag) {
							this.eliminate(res1.start, res1.end)
						}
						else if (res2.flag) {
							this.eliminate(res2.start, res2.end)
						}
						else {
							this.swap(this.first, this.second)
						}

						this.matrix[this.first.x][this.first.y].isSelected = false
						this.matrix[this.second.x][this.second.y].isSelected = false
						this.selected = false
					}
					else {
						this.matrix[this.first.x][this.first.y].isSelected = false
						this.first.x = Number(event.target.attributes.x.value)
						this.first.y = Number(event.target.attributes.y.value)
					}
				}
			},

			//检查选中的两个宝石能否交换（必须相邻）
			checkGemConnection (first, second) {
				if ((Math.abs(first.x - second.x) == 1 && Math.abs(first.y - second.y) == 0) 
					|| (Math.abs(first.y - second.y) == 1 && Math.abs(first.x - second.x) == 0)) {
					return true
				}
				else {
					return false
				}
			},

			//交换矩阵中选中的两个宝石位置
			swap (first, second) {
				var temp

				temp = this.matrix[first.x][first.y].x
				this.matrix[first.x][first.y].x = this.matrix[second.x][second.y].x
				this.matrix[second.x][second.y].x = temp

				temp = this.matrix[first.x][first.y].y
				this.matrix[first.x][first.y].y = this.matrix[second.x][second.y].y
				this.matrix[second.x][second.y].y = temp

				temp = this.matrix[first.x][first.y]
				this.matrix[first.x][first.y] = this.matrix[second.x][second.y]
				this.matrix[second.x][second.y] = temp
			},

			//检查交换后状态是否可以消除（5*5邻域内判断）
			//先判断是否存在3个相连，若存在，沿该方向继续寻找更多的相连匹配
			//三个相连的6种情况：
			//*****			*****			**o**			*****			*****			*****
			//*****			*****			**o**			*****			*****			**o**
			//ooo**			**ooo			**o**			**o**			*ooo*			**o**
			//*****			*****			*****			**o**			*****			**o**
			//*****			*****			*****			**o**			*****			*****
			checkGemElimination (pos) {
				var ret = {flag: false, start: {}, end: {}}
				var curType = this.matrix[pos.x][pos.y].val
				var neighbor = this.getNeighbor(pos)

				//case 1
				if (curType == neighbor[0] && curType == neighbor[1]) {
					ret.flag = true
					ret.start.x = pos.x
					ret.start.y = pos.y - 2
					ret.end.x = pos.x
					ret.end.y = pos.y
				}

				//case 2
				if (curType == neighbor[2] && curType == neighbor[3]) {
					ret.flag = true
					ret.start.x = pos.x
					ret.start.y = pos.y
					ret.end.x = pos.x
					ret.end.y = pos.y + 2
				}

				//case 3
				if (curType == neighbor[4] && curType == neighbor[5]) {
					ret.flag = true
					ret.start.x = pos.x - 2
					ret.start.y = pos.y
					ret.end.x = pos.x
					ret.end.y = pos.y
				}

				//case 4
				if (curType == neighbor[6] && curType == neighbor[7]) {
					ret.flag = true
					ret.start.x = pos.x
					ret.start.y = pos.y
					ret.end.x = pos.x + 2
					ret.end.y = pos.y
				}

				//case 5
				if (curType == neighbor[1] && curType == neighbor[2]) {
					ret.flag = true
					ret.start.x = pos.x
					ret.start.y = pos.y - 1
					ret.end.x = pos.x
					ret.end.y = pos.y + 1
				}

				//case 6
				if (curType == neighbor[5] && curType == neighbor[6]) {
					ret.flag = true
					ret.start.x = pos.x - 1 
					ret.start.y = pos.y
					ret.end.x = pos.x + 1
					ret.end.y = pos.y
				}

				//TODO：在相连的方向上延伸查找
				console.log(ret)
				return ret
			},

			//消除3个以上连着的宝石，并补充矩阵
			//分行消除，列消除两种情况
			eliminate (start, end) {
				//行消除
				if (start.x == end.x) {
					let len = end.y - start.y + 1

					// //消除
					// for (var i = start.y; i <= end.y; i++) {
					// 	this.matrix[start.x].splice(i, 1)
					// }

					//上方原有的宝石下降
					for (var i = start.x - 1; i >= 0; i--) {
						for (var j = start.y; j <= end.y; j++) {
							this.matrix[i + 1][j] = this.matrix[i][j]
							this.matrix[i + 1][j].x += 1
						}
					}

					//从顶部补充新宝石
					for (var i = start.y; i <= end.y; i++) {
						var temp = {}
						temp.val = Math.ceil(Math.random() * 7)			//宝石的值（即颜色）
						temp.color = this.colors[temp.val - 1]			//宝石颜色
						temp.isSelected = false											//宝石被选中的状态
						temp.x = start.x 														//当前宝石横坐标
						temp.y = i																	//当前宝石纵坐标

						this.matrix[0][i] = temp
					}

					//TODO: checkGemElimination
				}
				//列消除
				else {
					let len = end.x - start.x + 1

					// //消除
					// for (var i = start.x; i <= end.x; i++) {
					// 	this.matrix[i].splice(start.y, 1)
					// }

					//上方原有的宝石下落
					for (var i = start.x - 1; i >= 0; i--) {
						this.matrix[i + len][start.y] = this.matrix[i][start.y]
						this.matrix[i + len][start.y].x += len
					}

					//从顶部补充新宝石
					for (var i = 0; i < len; i++) {
						var temp = {}
						temp.val = Math.ceil(Math.random() * 7)			//宝石的值（即颜色）
						temp.color = this.colors[temp.val - 1]			//宝石颜色
						temp.isSelected = false											//宝石被选中的状态
						temp.x = i 																	//当前宝石横坐标
						temp.y = start.y														//当前宝石纵坐标

						this.matrix[i][start.y] = temp
					}

					//TODO: checkGemElimination
				}
			},

			//求出当前位置宝石的邻域
			//返回的结果为[左2左1，右1右2，上2上1，下1下2]
			//对边角情况特殊处理，不可达位置记为0
			//邻域为：
			//**o**
			//**o**
			//oo*oo
			//**o**
			//**o**
			getNeighbor (pos) {
				var ret = []

				if (pos.x - 2 >= 0)
					ret[4] = this.matrix[pos.x - 2][pos.y].val
				else
					ret[4] = 0

				if (pos.x - 1 >= 0)
					ret[5] = this.matrix[pos.x - 1][pos.y].val
				else
					ret[5] = 0

				if (pos.x + 1 <= 7)
					ret[6] = this.matrix[pos.x + 1][pos.y].val
				else
					ret[6] = 0

				if (pos.x + 2 <= 7)
					ret[7] = this.matrix[pos.x + 2][pos.y].val
				else
					ret[7] = 0

				if (pos.y - 2 >= 0)
					ret[0] = this.matrix[pos.x][pos.y - 2].val
				else
					ret[0] = 0

				if (pos.y - 1 >= 0)
					ret[1] = this.matrix[pos.x][pos.y - 1].val
				else
					ret[1] = 0

				if (pos.y + 1 <= 7)
					ret[2] = this.matrix[pos.x][pos.y + 1].val
				else
					ret[2] = 0

				if (pos.y + 2 <= 7)
					ret[3] = this.matrix[pos.x][pos.y + 2].val
				else
					ret[3] = 0

				console.log(ret)
				return ret
			}
		}
	}
</script>

<style scoped>
.row {
	display: flex;
	flex-direction: row;
}

.gem {
	width: 60px;
	height: 60px;
	line-height: 80px;
	text-align: center;
	border: 1px solid #aaa;
	border-radius: 10px;
	margin: 10px;
}

.selected {
	opacity: 0.6;
}

.slide-fade-enter-active {
  transition: all .3s ease;
}
.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}
.slide-fade-enter, .slide-fade-leave-to
/* .slide-fade-leave-active for below version 2.1.8 */ {
  transform: translateY(240px);
  opacity: 0;
}
</style>