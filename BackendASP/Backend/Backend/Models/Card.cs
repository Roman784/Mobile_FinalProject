namespace Backend.Models
{
    public class Card
    {
        public int Id { get; set; }
        public int DeckId { get; set; }
        public string Term { get; set; } = string.Empty;
        public string Definition { get; set; } = string.Empty;
    }
}
