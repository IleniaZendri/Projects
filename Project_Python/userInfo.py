import time
class userInfo:
    def __init__(self, playercount):
        playerName = 'Giocatore' + str(playercount+1)
        self.Name = playerName
        self.Symbol = playercount+1
        self.totalScore = 0   

    def updateScore(self, scoreOrizontal, scoreVertical, scoreDiag):
        self.totalScore = 0
        self.score(scoreOrizontal)
        self.score(scoreVertical)
        self.score(scoreDiag)

    def score(self, scores):
        Score = {3: 2, 4 : 10, 5 : 50}
        for score, quantity in scores.items():
            partialScore = Score[score]
            if self.totalScore == 0:
                self.totalScore = partialScore * quantity  
            else:
                self.totalScore += partialScore * quantity
        if(self.totalScore >=50):
            print("Congratulazioni " +self.Name+ " hai Vinto!!!")
            time.sleep(10)
            exit()