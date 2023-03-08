from gametable import gametable
from utilities import cls
from userInfo import userInfo
cls()

playercountInput = input("Dichiarare Numero di giocatori:")
playercount = int(playercountInput)
table = gametable()
table.addPlayers(playercount)

userChoice = ""
while(userChoice != '-n'):
    userChoice = input("Digitare '-n' per iniziare una nuova partita '-e' per abbandonare e '-o' per le opzioni: \n")
    if(userChoice == '-o'):
        table.playersSettings()
        print("Scegliere il giocare dalla lista")
    if(userChoice == '-e'):
        exit()

if(userChoice == '-n'):
    userInput = input("Dichiarare la dimensione del tavolo di gioco:")
    if userInput == '-e':
        exit()
    N = int(userInput)
    table.tableStart(N)
    table.print()

while(True):
    for player in table.players: 
        xyCoord = input(str(player.Name)+" mossa x-y:\n")
        if(xyCoord == '-e'):
            break
        yCoord = ''
        xCoord = ''  
        for c in xyCoord:
            if(c.isdigit()):
                yCoord = yCoord + c
            else:
                xCoord = xCoord + c
        table.move(xCoord, int(yCoord)-1, player)
        table.print()
