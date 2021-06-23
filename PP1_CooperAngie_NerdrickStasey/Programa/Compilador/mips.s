.data
print_t2:   .asciiz "Ingresa tu edad: "
print_t3:   .asciiz "Tu edad es: "

.text
.globl main
main:
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