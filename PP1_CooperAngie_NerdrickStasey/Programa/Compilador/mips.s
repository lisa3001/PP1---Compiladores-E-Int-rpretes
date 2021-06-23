.data
print_t6:   .asciiz "El resultado es "

.text
.globl main
main:
     li  $t0, 6
     move  $t1, $t0
     li  $t2, 8
     move  $t3, $t2
     move $a0, $t1
     move $a1, $t3
     jal guardarRegistros
     jal mult2
     jal cargarRegistros
     move $t4, $v0
     move  $t5, $t4
     la  $t6, print_t6
     move $a0, $t6
     jal print_str
     move $a0, $t5
     jal print_int
     li  $t7, 0
     move  $v0, $t7
     j end
mult2:
     move $s7, $ra
     move $t0, $a0
     move $t1, $a1
     li  $t2, 2
     move $t2, $a2
     move  $v0, $t2
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