function validateAdd() {
			if ($.trim($('#jobName').val()) == '') {
				alert('name cannot be empty！');
				$('#jobName').focus();
				return false;
			}
			if ($.trim($('#jobGroup').val()) == '') {
				alert('group cannot be empty！');
				$('#jobGroup').focus();
				return false;
			}
			if ($.trim($('#cronExpression').val()) == '') {
				alert('cron cannot be empty！');
				$('#cronExpression').focus();
				return false;
			}
			if ($.trim($('#beanClass').val()) == '' && $.trim($('#springId').val()) == '') {
				$('#beanClass').focus();
				alert('Bean Class or spring id must be there');
				return false;
			}
			if ($.trim($('#methodName').val()) == '') {
				$('#methodName').focus();
				alert('Method name cannot be empty！');
				return false;
			}
			return true;
		}