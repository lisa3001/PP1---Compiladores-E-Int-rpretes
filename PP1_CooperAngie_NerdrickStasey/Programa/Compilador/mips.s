.data
print0_t4:   .asciiz "El resultado es "

.text
.globl main
main:
     li  $t0, 8
     move $a0, $t0
     jal guardarRegistros
     jal mult2
     jal cargarRegistros
     move $t0, $v0
     move  $t2, $t0
     li  $t3, 6
     add $t3, $t2 ,$t3
     move  $t2, $t3
     la  $t4, print0_t4
     move $a0, $t4
     jal print_str
     move $a0, $t2
     jal print_int
     li  $t5, 0
     move  $v0, $t5
     j end
mult2:
     move $v1, $ra
     move $t0, $a0
     li  $t1, 2
     move $a0, $t0
     move $a1, $t1
     jal guardarRegistros 
     jal Potencia 
     jal cargarRegistros 
     move $t1, $v0
     move  $v0, $t1
     jr $v1
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
     sub $sp, $sp, 4
     sw  $v1, 0($sp)
     jr $ra
cargarRegistros:
     lw $v1, 0($sp)
     addi $sp, $sp, 4
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
	move $v1, $ra
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
	jr $v1

ExponenteUno:
	move $v0, $t1 
	jr $v1

ForPotencia:
	seq $t3, $t5, $t2
	bgtz $t3, FinForPotencia
	mulo $t4, $t4, $t1
	addi $t5, $t5, 1
	jal ForPotencia

FinForPotencia:
	move $v0, $t4 
	jr $v1
end:
      li $v0, 10
       syscall