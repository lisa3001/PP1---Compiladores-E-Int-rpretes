.data
.text

 .globl main
main:
     j end
  li   $t1, var0_t0
  li   $t2, 2
  li   $t3, var1_t2
Print:
	li $v0, 4
     	syscall  
	jr $ra
ReadOption:
	li $v0, 5
	syscall
	move $v1, $v0
	jr $ra
end:
      li $v0, 10
       syscall