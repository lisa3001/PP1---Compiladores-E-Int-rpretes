.data
<<<<<<< HEAD
print_t2:   .asciiz "Ingresa tu edad: "
print_t3:   .asciiz "Tu edad es: "
=======
var0_t5:   .byte 'a'
var1_t7:   .asciiz "gol"
>>>>>>> 2343a4dac9a541ecf8662a394dee42881d85c91a

.text
.globl main
main:
<<<<<<< HEAD
     li  $t0, 2
     move  $t1, $t0
     la  $t2, print_t2
     move $a0, $t2
     jal print_str
     move $a0, $t1
     li $a1, 50
     jal read_int
     move $t1, $v0
     la  $t3, print_t3
     move $a0, $t3
     jal print_str
     move $a0, $t1
     jal print_int
     li  $t4, 1
     move  $v1, $t4
=======
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
>>>>>>> 2343a4dac9a541ecf8662a394dee42881d85c91a
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
end:
      li $v0, 10
       syscall