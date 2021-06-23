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
     move $t0, $a0
     move $t1, $a1
     li  $t2, 23
     li  $t3, 24
     sgtu $t2, $t2 ,$t3
     li  $t4, 0
     seq $t2, $t2 ,$t4
     beq $t2, 1, IF_0
     li  $t5, 1
     li  $t6, 0
     seq $t5, $t5 ,$t6
     beq $t5, 1, ELIF_0
     li  $t7, 1
     li  $t8, 0
     sne $t7, $t7 ,$t8
     beq $t7, 1, ELIF_1
     j ELSE_0
IF_0:
     li  $t9, 8
     move  $v1, $t9
     jr $s7
     j IF_0_END
ELIF_0:
     li  $t2, 4
     move  $v1, $t2
     jr $s7
     j IF_0_END
ELIF_1:
     li  $t2, 8
     move  $v1, $t2
     jr $s7
     j IF_0_END
ELSE_0:
     li  $t3, 10
     move  $v1, $t3
     jr $s7
IF_0_END:
     li  $t3, 0
     move  $v1, $t3
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
guardarRegistros:
     sub $sp, $sp, 4
     sw  $t0, 0($sp)
     sub $sp, $sp, 4
     sw  $t1, 0($sp)
     sub $sp, $sp, 4
     sw  $t2, 0($sp)
     sub $sp, $sp, 4
     sw  $t3, 0($sp)
     sub $sp, $sp, 4
     sw  $t4, 0($sp)
     sub $sp, $sp, 4
     sw  $t5, 0($sp)
     sub $sp, $sp, 4
     sw  $t6, 0($sp)
     sub $sp, $sp, 4
     sw  $t7, 0($sp)
     sub $sp, $sp, 4
     sw  $t8, 0($sp)
     sub $sp, $sp, 4
     sw  $t9, 0($sp)
     jr $racargarRegistros:
     lw $t9, 0($sp)
     addi $sp, $sp, 4
     lw $t8, 0($sp)
     addi $sp, $sp, 4
     lw $t7, 0($sp)
     addi $sp, $sp, 4
     lw $t6, 0($sp)
     addi $sp, $sp, 4
     lw $t5, 0($sp)
     addi $sp, $sp, 4
     lw $t4, 0($sp)
     addi $sp, $sp, 4
     lw $t3, 0($sp)
     addi $sp, $sp, 4
     lw $t2, 0($sp)
     addi $sp, $sp, 4
     lw $t1, 0($sp)
     addi $sp, $sp, 4
     lw $t0, 0($sp)
     addi $sp, $sp, 4
     jr $raend:
      li $v0, 10
       syscall