<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<title>123</title>
	<script type="text/javascript" src="jq/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
	<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
	<style type="text/css">
		.tab-container{
			margin-top: 20px;
			display: none;
		}
		.form-group .small-input{
			width: 60px;
		}
		.second-row{
			margin-top: 10px;
		}
		.construct-select {
			width: 50px;
		}
		.construct-select option {
			text-align: center;
		}
		.tab-container.active{
			display: block;
		}
		.result {
			margin-top: 50px;
		}
	</style>
</head>
<body>
	<ul id="myTabs" class="nav nav-tabs">
  		<li role="presentation" class="active"><a href="#first">普通建筑</a></li>
		<li role="presentation"><a href="#second">变电站</a></li>
		<li role="presentation"><a href="#third">架空塔杆</a></li>
	</ul>
	<div id="first" class="active tab-container">
  		<div class="container">
  			<div class="col-md-4">
  				<label>土壤分层情况</label>
				<select id="floor">
					<option value="1">均匀土壤</option>
					<option value="2">双层土壤</option>
				</select>
  			</div>
			<div class="col-md-4">
				<form class="form-inline">
				  <div class="form-group" id="floorp">
				    <label for="p">土壤电阻率</label>
				    <input type="text" class="form-control small-input" id="p">
				  </div>
				  <div class="form-group" id="floorp0">
				    <label for="p">上层土壤电阻率</label>
				    <input type="text" class="form-control small-input" id="p0">
				  </div>
				  <div class="form-group" id="floorh">
				    <label for="h">上层土壤厚度</label>
				    <input type="text" class="form-control small-input" id="h">
				  </div>
					  <div class="form-group" id="floorp1">
				    <label for="p1">下层土壤电阻率</label>
				    <input type="text" class="form-control small-input" id="p1">
				  </div>
				</form>
			</div>
			<div class="col-md-4">
				<label>土壤电阻率参考查询</label>
				<button class="btn btn-default" id="query">点击</button>
			</div>
  		</div>
		<div class="container second-row">
			<div class="col-md-4">
				<label>建筑防雷分类</label>
				<select class="construct-select" id="build">
					<option value="1">一类</option>
					<option value="2">二类</option>
					<option value="3">三类</option>
				</select>
			</div>
			<div class="col-md-4">
				<label>建筑周围环境</label>
				<select class="construct-select" id="enviroment">
					<option value="true">宽松</option>
					<option value="false">严格</option>
				</select>
			</div>
		</div>
		<div class="container">
			<div class="col-md-8">
				<form class="form-inline">
				  <div class="form-group">
				    <label for="area">建筑占地面积</label>
				    <input type="text" class="form-control" id="area">
				  </div>
				  <div class="form-group">
				    <label for="resistance">工频接地电阻要求值</label>
				    <input type="text" class="form-control" id="resistance">
				  </div>
				</form>
			</div>
		</div>
	</div>
	<div id="second" class="tab-container">
  		<div class="container">
  			<div class="col-md-4">
  				<label>土壤分层情况</label>
				<select id="floor">
					<option value="1">均匀土壤</option>
					<option value="2">双层土壤</option>
				</select>
  			</div>
			<div class="col-md-4">
				<form class="form-inline">
				  <div class="form-group">
				    <label for="p">土壤电阻率</label>
				    <input type="text" class="form-control small-input" id="p">
				  </div>
				  <div class="form-group" id="floorp0">
				    <label for="p">上层土壤电阻率</label>
				    <input type="text" class="form-control small-input" id="p0">
				  </div>
				  <div class="form-group" id="floorh">
				    <label for="h">上层土壤厚度</label>
				    <input type="text" class="form-control small-input" id="h">
				  </div>
					  <div class="form-group" id="floorp1">
				    <label for="p1">下层土壤电阻率</label>
				    <input type="text" class="form-control small-input" id="p1">
				  </div>
				</form>
			</div>
			<div class="col-md-4">
				<label>土壤电阻率参考查询</label>
				<button class="btn btn-default" id="query">点击</button>
			</div>
  		</div>
		<div class="container second-row">
			<div class="col-md-4">
				<label>变电站电压</label>
				<select class="construct-select" id="build">
					<option value="20">20KV</option>
					<option value="33">33KV</option>
					<option value="66">66KV</option>
					<option value="110">110KV</option>
					<option value="200">220KV</option>
					<option value="500">500KV</option>
				</select>
			</div>
			<div class="col-md-4">
				<label>环境控制</label>
				<select class="construct-select" id="enviroment">
					<option value="true">是</option>
					<option value="false">否</option>
				</select>
			</div>
		</div>
		<div class="container">
			<div class="col-md-8">
				<form class="form-inline">
				  <div class="form-group">
				    <label for="area">占地面积</label>
				    <input type="text" class="form-control" id="area">
				  </div>
				  <div class="form-group">
				    <label for="resistance">工频电阻要求值</label>
				    <input type="text" class="form-control" id="resistance">
				  </div>
				</form>
			</div>
		</div>
	</div>
	<div id="third" class="tab-container">
  		<div class="container">
  			<div class="col-md-4">
  				<label>层数</label>
				<select id="floor">
					<option value="1">1</option>
					<option value="2">2</option>
				</select>
  			</div>
			<div class="col-md-4">
				<form class="form-inline">
				  <div class="form-group">
				   <label for="p">土壤电阻率</label>
				    <input type="text" class="form-control small-input" id="p">
				  </div>
				  <div class="form-group" id="floorp0">
				    <label for="p">上层土壤电阻率</label>
				    <input type="text" class="form-control small-input" id="p0">
				  </div>
				  <div class="form-group" id="floorh">
				    <label for="h">上层土壤厚度</label>
				    <input type="text" class="form-control small-input" id="h">
				  </div>
					  <div class="form-group" id="floorp1">
				    <label for="p1">下层土壤电阻率</label>
				    <input type="text" class="form-control small-input" id="p1">
				  </div>
				</form>
			</div>
			<div class="col-md-4">
				<label>土壤电阻率参考查询</label>
				<button class="btn btn-default" id="query">点击</button>
			</div>
  		</div>
		<div class="container second-row">
			<div class="col-md-4">
				<label>建筑分类</label>
				<select class="construct-select" id="build">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
				</select>
			</div>
			<div class="col-md-4">
				<label>环境控制</label>
				<select class="construct-select" id="enviroment">
					<option value="true">是</option>
					<option value="false">否</option>
				</select>
			</div>
		</div>
	</div>
	<div class="result">
		<div>
			<label>接地装置设计:</label>
			<span id="design"></span>
		</div>
		<div>
			<label>工频接地电阻值:</label>
			<span id="r"></span>
		</div>
		<div>
			<label>冲击接地电阻值:</label>
			<span id="ri"></span>
		</div>
		<div>
			<label>接地耗材预算:</label>
			<span id="budget"></span>
		</div>
	</div>
</body>
<script type="text/javascript">
	$('#myTabs a').click(function(e) {
	  e.preventDefault()
	  $(this).tab('show')
	});

	$('#floor').change(function(e) {
		if($(this).val() == 2){
			$('#floorp').hide();
			$('#floorp0').show();
			$('#floorh').show();
			$('#floorp1').show();
		} else {
			$('#floorp').show();
			$('#floorp0').hide();
			$('#floorp1').hide();
			$('#floorh').hide();
		}
	});

	$("#query").click(function(e) {
		var p = $('#p').val();
		var h = $('#h').val();
		var p1 = $('#p1').val();
		var build = $('#build').val();
		var enviroment = $('#enviroment').val();
		var area = $('#area').val();
		var resistance = $('#resistance').val();
		var Datajson = {
			p: p,
			h: h,
			p1: p1,
			build: build,
			enviroment: enviroment,
			area: area,
			resistance: resistance
		}
		console.log(Datajson);
		// $.ajax({
		// 	url: "23/",//接口
		// 	data: JSON.stringify(Datajson),
		// 	success: function(o) {
		// 		if (typeof o == "string") {
		// 			o = JSON.parse(o);
		// 			$('#design').html(o.design);
		// 			$('#r').html(o.r);
		// 			$('#ri').html(o.ri);
		// 			$('#budget').html(o.budget);
		// 		} else {
		// 			console.log('error');
		// 		}
		// 	},
		// 	error: function(error) {
		// 		console.log(error)
		// 	}
		// })
	});
</script>
</html>