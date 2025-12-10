using System;
using System.Collections.Generic;

namespace Backend.Models;

public partial class Card
{
    public int Id { get; set; }

    public int DeckId { get; set; }

    public string Term { get; set; } = null!;

    public string Definition { get; set; } = null!;

    public DateTime? CreatedAt { get; set; }

    public Deck? Deck { get; set; }
}
