.data

.text
.globl main
main:
     li  $t0, 0
     move  $t1, $t0
     li  $t2, 0
     move  $t3, $t2
     li  $t4, 5
     sltu $t3, $t3 ,$t4
     move  $t5, $t3
     beq $t5, 1, end
     li  $t6, 1
     add $t1, $t1 ,$t6
     add $t3, $t3, 1
     move  $t3, $t3
     j for_0
     li  $t7, 0
     move  $v0, $t7
     j end
<<<<<<< HEAD
=======
suma:
     move $s7, $ra
<<<<<<< HEAD
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
=======
     li  $t0, a
     li  $t1, b
     li  $t2, 3
     li  $t3, 2
     add $t2, $t2 ,$t3
     move  $v0, $t2
>>>>>>> 70643d90bae15f71ab86f7140c592e6e3c82a422
     jr $s7
>>>>>>> 602c9d8a522a4a6325fe05b910acc09897c609fe
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
<<<<<<< HEAD
     jr $racargarRegistros:
=======
     jr $ra
cargarRegistros:
>>>>>>> 70643d90bae15f71ab86f7140c592e6e3c82a422
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
<<<<<<< HEAD
     jr $raend:
=======
     jr $ra
Potencia:
	move $s7, $ra
	move $t1, $a0
	move $t2, $a1
	seq $t3, $t2, $zero
	bgtz $t3, ExponenteCero
	li $t4, 1
	seq $t3, $t2, $t4
	neg $t3, $t3
	beq $t3, -1 ,ExponenteUno
	sub $t2, $t2, 1 
	move $t4, $t1
	li   $t5, 0
	jal ForPotencia

ExponenteCero:
	li $v0, 1 
	jr $s7

ExponenteUno:
	move $v0, $t1 
	jr $s7

ForPotencia:
	seq $t3, $t5, $t2
	bgtz $t3, FinForPotencia
	mulo $t4, $t4, $t1
	addi $t5, $t5, 1
	jal ForPotencia

FinForPotencia:
	move $v0, $t4 
	jr $s7
end:
>>>>>>> 70643d90bae15f71ab86f7140c592e6e3c82a422
      li $v0, 10
       syscall