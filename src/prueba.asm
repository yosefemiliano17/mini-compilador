        title exa3
        .model small
        .stack 100h
        .data
        
true  DB     1
false DB     0
NUM   DB     6 dup("$")
a     DW     ?

        .code
main    proc        far
        .startup

        MOV a, 123
       MOV DL, 10
       MOV AX, a
       DIV DL
       ADD AH, 48
       MOV NUM[4], AH
       CBW
       DIV DL
       ADD AH, 48
       MOV NUM[3], AH
       CBW
       DIV DL
       ADD AH, 48
       MOV NUM[2], AH
       CBW
       DIV DL
       ADD AH, 48
       MOV NUM[1], AH
       CBW
       DIV DL
       ADD AH, 48
       MOV NUM[0], AH
       MOV BX, 0001H
       LEA DX, NUM
       MOV AH, 09H
       INT 21H

       MOV AH, 02H
       MOV DL, 0DH
       INT 21H
       MOV DL, 0AH
       INT 21H
        .exit
main    endp
        end         main
        