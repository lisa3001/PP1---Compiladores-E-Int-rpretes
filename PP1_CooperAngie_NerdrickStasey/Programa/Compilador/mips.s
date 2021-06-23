.data
print_t7:   .asciiz "La edad nueva es "

.text
.globl main
main:
<<<<<<< HEAD
=======
<<<<<<< HEAD
     li  $t0, 123
     move  $t1, $t0
     li  $t2, 0
     move  $v0, $t2
=======
<<<<<<< HEAD
     li  $t0, 19
     move  $t1, $t0
     li  $t2, 18
     sgtu $t2, $t1 ,$t2
     beq $t2, 1, IF_0
     j ELSE_0
IF_0:
     li  $t3, 3
     li  $t4, 6
     mulo $t3, $t3 ,$t4
     move  $t5, $t3
     j IF_0_END
ELSE_0:
     li  $t6, 3
     move  $t7, $t6
     la  $t8, var3_t8
     move  $t9, $t8
IF_0_END:
     li  $t0, 0
     move  $v0, $t0
=======
>>>>>>> 14dce2eaf80192d351987f8aed323536cf7e43eb
     li  $t0, 1
     move  $s1, $t0
     li  $t2, 0
     move  $t3, $t2
FOR_0:
     li  $t4, 5
     sltu $t4, $t3 ,$t4
     move  $t5, $t4
     beq $t5, 0, end_for_0
     li  $t6, 5
     add $t6, $s1 ,$t6
     move  $s1, $t6
     add $t3, $t3, 1
     move  $t3, $t3
     j For_0
end_for_0:
     la  $t7, print_t7
     move $a0, $t7
     jal print_str
     move $a0, $s1
     jal print_int
     li  $t8, 0
     move  $v0, $t8
<<<<<<< HEAD
=======
>>>>>>> d55506b001cf56ed07afd4dac44b292301ee6a78
>>>>>>> 81345f279e657a96e1d4a614cb21b3b0b905b1f3
>>>>>>> 14dce2eaf80192d351987f8aed323536cf7e43eb
     j end
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
     jr $ra
cargarRegistros:
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
      li $v0, 10
       syscall