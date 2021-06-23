.data
var0_t5:   .byte 'a'
var1_t7:   .asciiz "gol"

.text
.globl main
main:
     li  $t0, 1
     li  $t1, 0
     seq $t0, $t0 ,$t1
     beq $t0, 1, IF_0
     li  $t2, 1
     li  $t3, 0
     sne $t2, $t2 ,$t3
     beq $t2, 1, ELIF_0
     j ELSE_0
IF_0:
     li  $t4, 4
     move  $v1, $t4
     j IF_0_END
ELIF_0:
     la  $t5, var0_t5
     move  $t6, $t5
     la  $t7, var1_t7
     move  $t8, $t7
     li  $t9, 8
     move  $v1, $t9
     j IF_0_END
ELSE_0:
     li  $t0, 10
     move  $v1, $t0
IF_0_END:
     li  $t1, 0
     move  $v1, $t1
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