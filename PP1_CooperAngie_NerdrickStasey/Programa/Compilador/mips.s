.data

.text
.globl main
main:
     li  $t0, 2
     li  $t1, 4
     seq $t0, $t0 ,$t1
     move  $t2, $t0
     li  $t3, 2
     li  $t4, 4
     sne $t3, $t3 ,$t4
     move  $t5, $t3
     li  $t6, 1
     move  $v1, $t6
     j end
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