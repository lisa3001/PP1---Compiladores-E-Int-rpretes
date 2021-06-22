.data

.text
.globl main
main:
     li  $t0, 3
     li  $t1, 3
     mulo $t0, $t0 ,$t1
     move  $t2, $t0
     move  $v1, $t2
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