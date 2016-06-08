OqFazerController.controller('UserController', function($scope,$http,$timeout,$sce,UserService) {

	TelaHelper.tela = 'user';
	$scope.user = {};
	$scope.users = [];
	$scope.currentPage = 1;
	$scope.itemsPerPage = 5;
	$scope.buildList = _buildList;

	function init(){
		$scope.loadUsers();
	}
	
	$scope.loadUsers = function() {
		UserService.searchAll().then(function (response){
			$scope.buildList(response);
		});
	};

	$scope.openModalUser = function(id, flag) {
		if(flag == "update"){
			$scope.update(id);
		}else{
			$scope.user = null;
		}
		jQuery('#modalFormUser').modal('show');
	};
	
	$scope.insert = function() {
		var data = {context : {user : $scope.user}};
		
		UserService.insert(data).then(function(response){
			$scope.user = null;
			$scope.loadUsers();
			$scope.cancelModal();
		})
	};
	
	$scope.deleta = function(id) {
		var data = {context : {user : {id : id}}};
		
		UserService.deleta(data).then(function(response){
			$scope.user = null;
			$scope.loadUsers();
		})
	};
	
	$scope.update = function(id) {
		var data = {context : {user : {id : id}}};
		
		UserService.update(data).then(function(response){
			$scope.user = response.context.user;
		})
	};
	
	$scope.cancelModalUser = function() {
		$scope.user = {};
		closeModalUser();
	};

	function _buildList(response) {
		$scope.users = response.data.context.users;
		$scope.currentPage = 1;
		$scope.$applyAsync();
	}
	
	function closeModalUser() {
		jQuery('#modalFormUser').modal('hide');
	};
	
	setTimeout(function() {
		$scope.loadUsers();
	}, 0);
	
	init();
});