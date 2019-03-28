import System.IO
import Control.Monad.State.Lazy
import Data.Maybe

main :: IO()
main = do
  handle <- openFile "input/day2.txt" ReadMode  
  contents <- hGetContents handle
  instructions <- pure $ lines contents
  putStrLn $ show (solve instructions)

data Direction = U | L | R | D
  deriving (Eq, Show)

parseDirection :: Char -> Maybe Direction
parseDirection 'U' = Just U
parseDirection 'D' = Just D
parseDirection 'L' = Just L
parseDirection 'R' = Just R
parseDirection _ = Nothing

parseLine :: String -> Maybe [Direction]
parseLine = traverse parseDirection

data Keypad = K1 | K2 | K3 | K4 | K5 | K6 | K7 | K8 | K9
  deriving (Show)

move :: Keypad -> Direction -> Maybe Keypad
move K1 R = Just K2
move K1 D = Just K4
move K1 _ = Nothing

move K2 R = Just K3
move K2 L = Just K1
move K2 D = Just K5
move K2 _ = Nothing

move K3 L = Just K2
move K3 D = Just K6
move K3 _ = Nothing

move K4 U = Just K1
move K4 R = Just K5
move K4 D = Just K7
move K4 _ = Nothing

move K5 U = Just K2
move K5 R = Just K6
move K5 L = Just K4
move K5 D = Just K8

move K6 L = Just K5
move K6 U = Just K3
move K6 D = Just K9
move K6 _ = Nothing

move K7 U = Just K4
move K7 R = Just K8
move K7 _ = Nothing

move K8 L = Just K7
move K8 U = Just K5
move K8 R = Just K9
move K8 _ = Nothing

move K9 U = Just K6
move K9 L = Just K8
move K9 _ = Nothing

moveDefault ::  Keypad -> Direction -> Keypad
moveDefault k d = fromMaybe k (move k d)

moveS :: Direction -> State Keypad ()
moveS dir = do
  current <- get
  next <- pure $ (moveDefault current dir)
  put next

reduceLine :: [Direction] -> State Keypad Keypad
reduceLine dirs = traverse moveS dirs >> get

findCombination :: [[Direction]] -> State Keypad [Keypad]
findCombination rows = traverse reduceLine rows

parse :: [String] -> Maybe [[Direction]]
parse = traverse parseLine

solve :: [String] -> Maybe [Keypad]
solve instructions = do
  rows <- parse instructions
  s <- pure $ findCombination rows
  pure $ fst $ runState s K5
