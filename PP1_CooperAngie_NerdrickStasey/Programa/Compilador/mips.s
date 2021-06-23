.data

.text
.globl main
main:
     li  $t0, 1
     li  $t1, 8
     move  $t0, $turn
     li  $t3, 0
     move  $v1, $t3
     j end
extra:
     li  $tra, num
     move  $tra, $to
     li  $t0, 23
     li  $t1, 24
     sgtu $t0, $t0 ,$t1
     li  $t2, 0
     seq $t0, $t0 ,$t2
     beq $t0, 1, IF_0
     li  $t3, 1
     li  $t4, 0
     seq $t3, $t3 ,$t4
     beq $t3, 1, ELIF_0
     li  $t5, 1
     li  $t6, 0
     sne $t5, $t5 ,$t6
     beq $t5, 1, ELIF_1
     j ELSE_0
IF_0:
     li  $t7, 8
     move  $v1, $t7
     j IF_0_END
ELIF_0:
     li  $t8, 4
     move  $v1, $t8
     j IF_0_END
ELIF_1:
     li  $t9, 8
     move  $v1, $t9
     j IF_0_END
ELSE_0:
     li  $t0, 10
     move  $v1, $t0
IF_0_END:
     li  $t0, 23
     li  $t1, 24
     sgtu $t0, $t0 ,$t1
     li  $t1, 0
     seq $t0, $t0 ,$t1
     beq $t0, 1, IF_1
     li  $t2, 1
     li  $t3, 0
     seq $t2, $t2 ,$t3
     beq $t2, 1, ELIF_2
     li  $t4, 1
     li  $t5, 0
     sne $t4, $t4 ,$t5
     beq $t4, 1, ELIF_3
     j ELSE_1
IF_1:
     li  $t6, 8
     move  $v1, $t6
     j IF_1_END
ELIF_2:
     li  $t7, 4
     move  $v1, $t7
     j IF_1_END
ELIF_3:
     li  $t7, 8
     move  $v1, $t7
     j IF_1_END
ELSE_1:
     li  $t8, 10
     move  $v1, $t8
IF_1_END:
     li  $t8, 0
     move  $v1, $t8
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