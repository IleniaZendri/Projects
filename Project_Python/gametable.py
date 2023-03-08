from utilities import cls
from userInfo import userInfo
import string

class gametable:

    def tableStart(self,N): 
        self.table = [[0 for x in range(N)] for x in range(N)]

    def print(self):
        cls()
        printNumbers = True
        for i in range(len(self.table)): 
            printY = True
            if printNumbers:
                for idx in range(len(self.table)+1): 
                    if idx != 0:
                        print(idx, end=' ')
                    else:
                        print(' ', end=' ')
                print() 
                printNumbers = False
            for j,x in enumerate(self.table): 
                if printY:
                    print(chr(i+97), end=' ')
                    printY = False
                if j < 9:
                    print(x[i], end =' ') 
                else:
                    print(' '+str(x[i]), end=' ')
            print() 
        print()
        for player in self.players:
            print(str(player.Name)+" punteggio: " + str(player.totalScore))
        print()

    def move(self, xCoord, yCoord, user):
        for idxY, valY in enumerate(self.table):
            if idxY == yCoord:                
                for idxX, valX in enumerate(valY):
                    if idxX == ord(xCoord) -97:
                        if valX == 0:
                            valY[idxX] = user.Symbol
                        else:
                            print('Campi giá in uso selezionare un campo vuoto')
                            xyCoord = input(str(user.Name)+" mossa lettera-numero:\n")
                            yCoord = ''
                            xCoord = ''  
                            for c in xyCoord:
                                if(c.isdigit()):
                                    yCoord = yCoord + c
                                else:
                                    xCoord = xCoord + c
                            self.move(xCoord, int(yCoord)-1, user)
        self.evaluate(user)
    
    def addPlayers(self, size):
        self.players = []
        for _ in range(0,size):
            self.players.append(userInfo(len(self.players)))
    
    def evaluate(self, user):
        self.scoreVertical = dict()
        self.scoreOrizontal = dict()
        self.scoreDiag = dict()
        usedDiagFwd = []
        usedDiagBck = []
        score = 0
        scoreOrizontal = 0

        #verticale
        for idx, column in enumerate(self.table): 
            for i in range(0, len(column)-1):
                if column[i] != 0:
                    if column[i] == user.Symbol:
                        score+=1
                else:
                    if score > 2:
                        if score > 5:
                            score = 5
                        self.scoreVertical[score] = 1 if score not in self.scoreVertical else self.scoreVertical[score] + 1                        
                    score=0

            #orrizzontale
            for row in self.table:
                if row[idx] != 0:
                    if row[idx] == user.Symbol: 
                        scoreOrizontal += 1
                else:
                    if scoreOrizontal > 2:
                        if scoreOrizontal > 5:
                            scoreOrizontal = 5
                        self.scoreOrizontal[scoreOrizontal] = 1 if scoreOrizontal not in self.scoreOrizontal else self.scoreOrizontal[scoreOrizontal] + 1
                    scoreOrizontal = 0
           
            #Gestire diagonale da dx a sx
            for j in range(len(column)):
                if column[j] == user.Symbol:
                    if str(idx) + str(j) not in usedDiagFwd:
                        incr = 1
                        potentialUsedDiag = []
                        while(idx + incr < len(column) and j + incr < len(column) and column[j] == self.table[idx + incr][j + incr]):
                            potentialUsedDiag.append(str(idx + incr - 1)+str(j + incr - 1))
                            incr+=1
                        if incr > 2:
                            potentialUsedDiag.append(str(idx + incr - 1)+str(j + incr - 1))
                            if incr > 5:
                                incr = 5
                            self.scoreDiag[incr] = 1 if incr not in self.scoreDiag else self.scoreDiag[incr] + 1
                            usedDiagFwd.extend(potentialUsedDiag)

            #Gestire diagonale da sx a dx
            for j in range(len(column)-1, 0, -1):
                if column[j] == user.Symbol:
                    if str(idx) + str(j) not in usedDiagBck:
                        incr = - 1
                        potentialUsedDiag = []
                        while(idx - incr >= 0 and idx - incr < len(column) and j + incr < len(column) and j + incr >= 0 and column[j] == self.table[idx - incr][j + incr]):
                            potentialUsedDiag.append(str(idx - incr - 1)+str(j + incr + 1))
                            incr-=1
                        if incr < - 2:
                            potentialUsedDiag.append(str(idx - incr - 1)+str(j + incr + 1))
                            if incr < -5:
                                incr = -5
                            absIncr = abs(incr)
                            self.scoreDiag[absIncr] = 1 if absIncr not in self.scoreDiag else self.scoreDiag[absIncr] + 1
                            usedDiagBck.extend(potentialUsedDiag)
        user.updateScore(self.scoreOrizontal, self.scoreVertical, self.scoreDiag)
    
    def playersSettings(self):
        for i,x in enumerate(self.players):
            print('Giocatore ' + str(i+1) + ' --> Nome attuale: ' + x.Name + ' Simbolo attuale: ' + str(x.Symbol))
        userInput = input('Quale giocatore si intende modificare? (indicare il numero): ')
        if userInput.isdigit():
            player = self.players[int(userInput) - 1] 
            userInput = input('Digitare Nome o Simbolo per modificare i rispettivi parametri: ')   
            if userInput.lower() == 'nome':
                newName = input("Digitare il nuovo nome: ")
                player.Name = newName
            elif userInput.lower() == 'simbolo':
                newSimbol = input('Digitare il nuovo simbolo: ')
                player.Symbol = newSimbol