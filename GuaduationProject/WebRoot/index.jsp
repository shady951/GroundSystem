<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<title>防雷接地计算</title>
	<script type="text/javascript" src="/GuaduationProject/jq/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="<%=path%>/bootstrap/js/bootstrap.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=path%>/bootstrap/css/bootstrap.min.css">
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
			width: 60px;
		}
		.construct-select option {
			text-align: center;
		}
		.tab-container.active{
			display: block;
		}
		.result {
			margin-top: 80px;
		}
		.tmargin{
			margin-top:8px;
			margin-bottom:8px;
		}
	</style>
</head>
<body >
	<div style="position:absolute; width:100%; height:100%; z-index:-1">    
	<img id="Layer" src="<%=path %>/img/building.png" height="100%" width="100%"/>
	</div> 
	<ul id="myTabs" class="nav nav-tabs">
  		<li role="presentation" class="active"><a href="#first" data-val="1">普通建筑</a></li>
		<li role="presentation"><a href="#second"  data-val="2">变电站</a></li>
		<li role="presentation"><a href="#third"  data-val="3">架空塔杆</a></li>
	</ul>
	<div id="first" class="active tab-container">
		<div>
			<img style="bac" src="">
		</div>
			<div style="text-align:center; margin-bottom:50px; margin-top:30px">
				<h1>防雷接地计算系统</h1>
			</div>
			<div class="container tmargin">
			<div class="col-md-6 text-right">
				<label>建筑防雷分类</label>
				<select class="construct-select type">
					<option value="1">一类</option>
					<option value="2">二类</option>
					<option value="3">三类</option>
				</select>
			</div>
			<div class="col-md-6">
				<label>建筑周围环境</label>
				<select class="construct-select city">
					<option value="true">严格</option>
					<option value="false">宽松</option>
				</select>
			</div>
		</div>
  			<div class="container">
  			<div class="col-md-6 text-right " style="margin-top:6px">
  				<label >土壤分层情况</label>
				<select class="floor" style="width:85px;">
					<option value="1">均匀土壤</option>
					<option value="2">双层土壤</option>
				</select>
  			</div>
				<form class="form-inline">
				  <div class="form-group floorp col-md-6 ">
				    <label for="1p">土壤电阻率</label>
				    <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control small-input p" id="1p">
				    <label for="1p">欧姆</label>
				    <button class="btn btn-default" type="button" data-toggle="modal" data-target="#myModal">查询</button>
				  </div>
				  <div class="form-group floorp0 col-md-6 ">
				    <label for="1p0">上层土壤电阻率</label>
				    <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control small-input p0" id="1p0">
				    <label for="1p0">欧姆</label>
				    <button class="btn btn-default" type="button" data-toggle="modal" data-target="#myModal">查询</button>
				  </div>
					  <div class="form-group floorH col-md-6 text-right">
					    <label for="1H">上层土壤厚度</label>
					    <input type="text" onkeyup="value=value.replace(/[^\d.]/g,'')" class="form-control small-input H" id="1H">
					    <label for="1H">米</label>
					  </div>
						  <div class="form-group floorp1 col-md-6">
					    <label for="1p1">下层土壤电阻率</label>
					    <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control small-input p1" id="1p1">
					    <label for="1p1">欧姆</label>
				  </div>
				</form>
  		</div>
		<div class="container tmargin">
				<form class="form-inline">
				  <div class="form-group col-md-6 text-right">
				    <label for="S">建筑占地面积</label>
				    <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control small-input S" style="width:70px">
				     <label for="S">平方米</label>
				  </div>
				  <div class="form-group col-md-6">
				    <label for="Rk">工频接地电阻要求值</label>
				    <input type="text" onkeyup="value=value.replace(/[^\d.]/g,'')" class="form-control small-input Rk">
				    <label for="Rk">欧姆</label>
				  </div>
				</form>
		</div>
	</div>
	<div id="second" class="tab-container">
  		<div>
			<img style="bac" src="">
		</div>
			<div style="text-align:center; margin-bottom:50px; margin-top:30px">
				<h1>防雷接地计算系统</h1>
			</div>
			<div class="container tmargin">
			<div class="col-md-6 text-right">
				<label>电压等级</label>
				<select class="construct-select type" style="width:70px">
					<option value="35">35KV</option>
					<option value="66">66KV</option>
					<option value="110">110KV</option>
					<option value="220">220KV</option>
					<option value="330">330KV</option>
					<option value="500">500KV</option>
				</select>
			</div>
			<div class="col-md-6">
				<label>建筑周围环境</label>
				<select class="construct-select city">
					<option value="true">严格</option>
					<option value="false">宽松</option>
				</select>
			</div>
		</div>
  			<div class="container">
  			<div class="col-md-6 text-right " style="margin-top:6px">
  				<label >土壤分层情况</label>
				<select class="floor" style="width:85px;">
					<option value="1">均匀土壤</option>
					<option value="2">双层土壤</option>
				</select>
  			</div>
				<form class="form-inline">
				  <div class="form-group floorp col-md-6 ">
				    <label for="1p">土壤电阻率</label>
				    <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control small-input p" id="1p">
				    <label for="1p">欧姆</label>
				    <button class="btn btn-default" type="button" data-toggle="modal" data-target="#myModal">查询</button>
				  </div>
				  <div class="form-group floorp0 col-md-6 ">
				    <label for="1p0">上层土壤电阻率</label>
				    <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control small-input p0" id="1p0">
				    <label for="1p0">欧姆</label>
				    <button class="btn btn-default" type="button" data-toggle="modal" data-target="#myModal">查询</button>
				  </div>
					  <div class="form-group floorH col-md-6 text-right">
					    <label for="1H">上层土壤厚度</label>
					    <input type="text" onkeyup="value=value.replace(/[^\d.]/g,'')" class="form-control small-input H" id="1H">
					    <label for="1H">米</label>
					  </div>
						  <div class="form-group floorp1 col-md-6">
					    <label for="1p1">下层土壤电阻率</label>
					    <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control small-input p1" id="1p1">
					    <label for="1p1">欧姆</label>
				  </div>
				</form>
			<!--  
			<div class="col-md-4">
				<label>土壤电阻率参考值</label>
				<button class="btn btn-default" data-toggle="modal" data-target="#myModal">查看</button>
			</div>
			-->
  		</div>
		<div class="container tmargin">
				<form class="form-inline">
				  <div class="form-group col-md-6 text-right">
				    <label for="S">建筑占地面积</label>
				    <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control small-input S" style="width:70px">
				     <label for="S">平方米</label>
				  </div>
				  <div class="form-group col-md-6">
				    <label for="Rk">工频接地电阻要求值</label>
				    <input type="text" onkeyup="value=value.replace(/[^\d.]/g,'')" class="form-control small-input Rk">
				    <label for="Rk">欧姆</label>
				  </div>
				</form>
		</div>
	</div>
	<div id="third" class="tab-container">
  		<div>
			<img style="bac" src="">
		</div>
			<div style="text-align:center; margin-bottom:50px; margin-top:30px">
				<h1>防雷接地计算系统</h1>
			</div>
			<div class="container tmargin">
			<div class="col-md-6 text-right">
				<label>杆塔类型</label>
				<select id="towertype" class="construct-select type" style="width:65px">
					<option value="1">铁塔型</option>
					<option value="2">混凝土杆型</option>
				</select>
			</div>
			<div class="col-md-6">
				<label>建筑周围环境</label>
				<select class="construct-select city">
					<option value="true">严格</option>
					<option value="false">宽松</option>
				</select>
			</div>
		</div>
  			<div class="container">
  			<div class="col-md-6 text-right " style="margin-top:6px">
  				<label >土壤分层情况</label>
				<select class="floor" style="width:85px;">
					<option value="1">均匀土壤</option>
					<option value="2">双层土壤</option>
				</select>
  			</div>
				<form class="form-inline">
				  <div class="form-group floorp col-md-6 ">
				    <label for="1p">土壤电阻率</label>
				    <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control small-input p" id="1p">
				    <label for="1p">欧姆</label>
				    <button class="btn btn-default" type="button" data-toggle="modal" data-target="#myModal">查询</button>
				  </div>
				  <div class="form-group floorp0 col-md-6 ">
				    <label for="1p0">上层土壤电阻率</label>
				    <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control small-input p0" id="1p0">
				    <label for="1p0">欧姆</label>
				    <button class="btn btn-default" type="button" data-toggle="modal" data-target="#myModal">查询</button>
				  </div>
					  <div class="form-group floorH col-md-6 text-right">
					    <label for="1H">上层土壤厚度</label>
					    <input type="text" onkeyup="value=value.replace(/[^\d.]/g,'')" class="form-control small-input H" id="1H">
					    <label for="1H">米</label>
					  </div>
						  <div class="form-group floorp1 col-md-6">
					    <label for="1p1">下层土壤电阻率</label>
					    <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control small-input p1" id="1p1">
					    <label for="1p1">欧姆</label>
				  </div>
				</form>
  		</div>
		<div id="towerS" class="container tmargin">
				<form class="form-inline">
				  <div class="form-group col-md-12 text-center">
				    <label for="S">建筑占地面积</label>
				    <input id="towerSinput" onkeyup="value=value.replace(/[^\d]/g,'')" type="text" class="form-control small-input S">
				     <label for="S">平方米</label>
				  </div>
				</form>
		</div>
	</div>
	<div class="btn btn-primary btn-lg" id="query" style="position: absolute;left: 0; right: 0;width: 200px; margin: 10px auto 0 auto;">开始设计</div>
	<div class="result" style="margin-left:27%; margin-right:27%">
		<div>
			<label>防雷接地装置设计:</label>
			<span id="plan"></span>
		</div>
		<div>
			<label>工频接地电阻值(欧姆):</label>
			<span id="R"></span>
		</div>
		<div>
			<label>冲击接地电阻值(欧姆):</label>
			<span id="Ri"></span>
		</div>
		<div>
			<label>接地耗材预算(元):</label>
			<span id="money"></span>
		</div>
	</div>
	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">土壤电阻率参考值</h4>
	      </div>
	      <div class="modal-body">
	       <img  src="<%=path %>/img/res.png">
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	      </div>
	    </div>
	  </div>
	</div>
</body>
<script type="text/javascript">
	window.onload = ex;
	
	$('#myTabs a').click(function(e) {
	  e.preventDefault()
	  $(this).tab('show');
	  changepic();
	   $('#plan').html('')
	   $('#R').html('')
	   $('#Ri').html('')
	   $('#money').html('')
	});
	
	function changepic() {
		var style = $('.active').find('a').data('val'); 
		switch(style) {
			case 1: $('#Layer').attr('src', '<%=path %>/img/building.png');
			break;
			case 2: $('#Layer').attr('src', '<%=path %>/img/powerstation1.png');
			break;
			case 3: $('#Layer').attr('src', '<%=path %>/img/tower.png');
			break;
		} 
	}
	
	
	function ex() {
		//console.log('ex a:'+$('.active .floor').val());
		//console.log('ex :'+$('.floor').val());
		if($('.active .floor').val() == 2){
				$('.floorp').hide();
				$('.floorp0').show();
				$('.floorH').show();
				$('.floorp1').show();
			} else {
				$('.floorp').show();
				$('.floorp0').hide();
				$('.floorp1').hide();
				$('.floorH').hide();
			}
			//console.log($('.active .floor').val());
			//console.log($('.active .floorp0'));
	}
	$('.floor').change(function(e) {
	//console.log('change a:'+$('.active .floor').val())
	//console.log('change:'+$('.floor').val())
		if($('.active .floor').val() == 2){
			$('.active .floorp').hide();
			$('.active .p').val('');
			$('.active .floorp0').show();
			$('.active .floorH').show();
			$('.active .floorp1').show();
		} else {
			$('.active .floorp').show();
			$('.active .p0').val('');
			$('.active .floorp0').hide();
			$('.active .p1').val('');
			$('.active .floorp1').hide();
			$('.active .H').val('');
			$('.active .floorH').hide();
		}
	});
	
	$('#towertype').change(function(e){
		if($('#towertype').val() == 2) {
			$('#towertype').attr('style', 'width:100px');
			$('#towerSinput').val('');
			$('#towerS').hide();
		} else {
			$('#towertype').attr('style', 'width:65px');
			$('#towerS').show();
		}
	});
	
	
	$('#myModal').on('shown.bs.modal', function () {
	  $('#myInput').focus()
	});
	
	$('#query').click(function(e) {
		var style = $('.active').find('a').data('val'); 
		if((!$('.active .Rk').val() && style != 3) || 
		((!$('.active .S').val() && style != 3) ||  (!$('.active .S').val() && style == 3 && $('#towertype').val() == 1))|| 
		(!$('.active .p').val() && $('.active .floor').val() == 1) || 
		($('.active .floor').val() == 2 && (!$('.active .p0').val() || !$('.active .p1').val() || !$('.active .H').val()))) {
			window.alert('请完善输入数据');
		} else {
		var p
		if($('.active .p').val()) {
			p = $('.active .p').val();
		} else {
			p = $('.active .p0').val();
		}
		var H = $('.active .H').val();
		var p1 = $('.active .p1').val();
		var S = $('.active .S').val();
		var Rk = $('.active .Rk').val();
		var city = $('.active .city').val();
		var type = $('.active .type').val();
		var Datajson = {
			style:style,
			p: p,
			H: H,
			p1: p1,
			type: type,
			city: city,
			S: S,
			Rk: Rk
		}
		console.log(Datajson);
		 $.ajax({
		 	url: '/GuaduationProject/design',
		 	type:'POST',
			dataType:'json',
		 	contentType:'application/json',
		 	data: JSON.stringify(Datajson),
		 	success: function(o) {
		 		if (typeof o == 'string') {
		 			o = JSON.parse(o);
		 			}
		 			$('#plan').html(o.plan);
		 			$('#R').html(o.R);
		 			$('#Ri').html(o.Ri);
		 			$('#money').html(o.money);
		 	},
		 	error: function(error) {
		 		console.log(error)
		 	}
		 })
		}
	});
</script>
</html>