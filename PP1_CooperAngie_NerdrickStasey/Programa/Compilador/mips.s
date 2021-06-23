.data

.text
.globl main
main:
     li $a0, 1
     li $a1, 3
     jal extra
     move $t0, $v0
     li  $t3, 0
     move  $v1, $t3
     j end
extra:
     move $s7, $ra
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
     jr $s7
     j IF_0_END
ELIF_0:
     li  $t8, 4
     move  $v1, $t8
     jr $s7
     j IF_0_END
ELIF_1:
     li  $t9, 8
     move  $v1, $t9
     jr $s7
     j IF_0_END
ELSE_0:
     li  $t0, 10
     move  $v1, $t0
     jr $s7
IF_0_END:
     li  $t0, 0
     move  $v1, $t0
     jr $s7
print_str:
	li $v0, 4
     	syscall  
	jr $ra
print_int:
	li $v0, 1
     	syscall  
	jr $ra
read_int:
	li $v0, 5
	syscall
	move $v1, $v0
	jr $ra
read_str:
	li $v0, 8
	syscall
	jr $ra
end:
      li $v0, 10
       syscall